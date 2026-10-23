package Communication.middleware;

import Communication.dto.ApiErrorResponse;
import Communication.entity.ErrorLogRecord;
import Communication.repository.ErrorLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ErrorLogRepository errorLogRepository;

    public GlobalExceptionHandler(ErrorLogRepository errorLogRepository) {
        this.errorLogRepository = errorLogRepository;
    }

    private void persistError(HttpServletRequest request, Exception ex) {
        String reqId = (String) request.getAttribute(CorrelationIdFilter.MDC_KEY);
        if (reqId == null) reqId = "N/A";

        ErrorLogRecord record = new ErrorLogRecord(
                reqId,
                request.getMethod(),
                request.getRequestURI(),
                ex.getClass().getSimpleName(),
                ex.getMessage() != null ? ex.getMessage() : "No message provided"
        );
        errorLogRepository.save(record);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        persistError(request, ex);

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiErrorResponse.withValidation(400, "Bad Request", "Validation failed", request.getRequestURI(), errors)
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDatabaseErrors(DataIntegrityViolationException ex, HttpServletRequest request) {
        persistError(request, ex);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiErrorResponse.of(409, "Conflict", "Database integrity constraint violated", request.getRequestURI())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericErrors(Exception ex, HttpServletRequest request) {
        persistError(request, ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiErrorResponse.of(500, "Internal Server Error", "An unexpected error occurred", request.getRequestURI())
        );
    }
}
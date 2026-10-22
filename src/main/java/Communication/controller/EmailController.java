package Communication.controller;

import Communication.dto.EmailRequestDto;
import Communication.dto.UpdateEmailBodyDto;
import Communication.entity.MessageRecord;
import Communication.services.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // POST /v1/email
    @PostMapping
    public ResponseEntity<MessageRecord> createItem(@Valid @RequestBody EmailRequestDto request) {
        MessageRecord createdRecord = emailService.processEmail(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecord);
    }

    // GET /v1/email/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MessageRecord> getItemById(@PathVariable UUID id) {
        return emailService.retrieveEmail(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PATCH /v1/email/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<MessageRecord> updateEmailBody(@PathVariable UUID id, @Valid @RequestBody UpdateEmailBodyDto request) {
        return emailService.processEmailUpdate(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /v1/email/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable UUID id) {
        boolean deleted = emailService.deleteEmail(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
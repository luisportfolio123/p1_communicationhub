package Communication.controller;

import Communication.dto.TextRequestDto;
import Communication.dto.UpdateTextBodyDto;
import Communication.entity.MessageRecord;
import Communication.services.TextService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/text")
public class TextController {

    private final TextService textService;

    public TextController(TextService textService) {
        this.textService = textService;
    }

    // POST /v1/text
    @PostMapping
    public ResponseEntity<MessageRecord> sendText(@Valid @RequestBody TextRequestDto request) {
        MessageRecord createdRecord = textService.processText(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecord);
    }

    // GET /v1/text/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MessageRecord> getTextById(@PathVariable UUID id) {
        return textService.retrieveText(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PATCH /v1/text/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<MessageRecord> updateTextBody(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTextBodyDto request) {
        return textService.processTextUpdate(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /v1/text/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteText(@PathVariable UUID id) {
        boolean deleted = textService.deleteText(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
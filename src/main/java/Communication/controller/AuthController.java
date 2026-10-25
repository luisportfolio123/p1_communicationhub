package Communication.controller;

import Communication.entity.ApiTokenRecord;
import Communication.services.AuthTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthTokenService authTokenService;

    public AuthController(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }

    @PostMapping("/tokens")
    public ResponseEntity<Map<String, Object>> createToken(@RequestBody Map<String, String> body) {
        String clientName = body.getOrDefault("clientName", "StandardClient");
        // Default expiry: 30 days
        ApiTokenRecord token = authTokenService.generateToken(clientName, Duration.ofDays(30));

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "token", token.getTokenValue(),
                "clientName", token.getClientName(),
                "expiresAt", token.getExpiresAt().toString()
        ));
    }
}
package com.example.demo.sanitization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent // Spring Boot automatically registers this deserializer with Jackson
public class StringSanitizerDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String rawValue = parser.getValueAsString();

        if (rawValue == null) {
            return null;
        }

        // 1. Trim surrounding whitespace
        String cleaned = rawValue.trim();

        // 2. Remove null bytes to prevent low-level byte injection
        cleaned = cleaned.replace("\0", "");

        // 3. Simple baseline sanitization: strip script tags or dangerous HTML markers
        // (For production HTML cleaning, tools like OWASP Java HTML Sanitizer are standard)
        cleaned = cleaned.replaceAll("(?i)<script.*?>.*?</script>", "")
                .replaceAll("(?i)<.*?javascript:.*?>", "");

        return cleaned;
    }
}
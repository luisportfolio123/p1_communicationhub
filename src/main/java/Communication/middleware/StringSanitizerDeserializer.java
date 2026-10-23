package Communication.middleware;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class StringSanitizerDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String raw = parser.getValueAsString();

        if (raw == null) {
            return null;
        }

        // 1. Remove dangerous null bytes (\0)
        String cleaned = raw.replace("\0", "");

        // 2. Strip embedded executable script blocks
        cleaned = cleaned.replaceAll("(?i)<script.*?>.*?</script>", "");

        // 3. Strip inline javascript pseudo-protocol attributes (e.g. href="javascript:...")
        cleaned = cleaned.replaceAll("(?i)javascript:", "");

        return cleaned;
    }
}
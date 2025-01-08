package qoraa.net.common.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonWriter {
    public static final JsonWriter DEFAULT = new JsonWriter(new ObjectMapper().writer());

    private final ObjectWriter writer;

    private JsonWriter(ObjectWriter writer) {
        this.writer = writer;
    }

    public String writeAsString(Object value) throws Exception {
        return writer.writeValueAsString(value);
    }
}

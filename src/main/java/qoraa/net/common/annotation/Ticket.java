package qoraa.net.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.StringUtils;
import qoraa.net.common.crypt.IdEncrypter;

import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Indicates that an annotated field will be automatically converted from String
 * to Long and vice versa.
 *
 * In this way, we will decrease the usage of {@link IdEncrypter}, so it will be
 * easier for us to change implementation for mapping Ids.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = Ticket.SimpleDeserializer.class)
@JsonSerialize(using = Ticket.SimpleSerializer.class)
@Documented
public @interface Ticket {

	class SimpleDeserializer extends JsonDeserializer<Object> {
		@Override
		public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			JsonNode node = p.getCodec().readTree(p);
			return node.isArray()
					? deserializeArray(node)
					: deserializeSingle(node.asText());
		}

		private Set<Long> deserializeArray(JsonNode node) {
			Set<String> tickets = new HashSet<>();
			node.forEach(n -> tickets.add(n.asText()));
			return tickets.stream()
					.map(IdEncrypter::decryptGenericId)
					.collect(Collectors.toSet());
		}

		private Long deserializeSingle(String text) {
			return StringUtils.isBlank(text) ? null : IdEncrypter.decryptGenericId(text);
		}
	}

	class SimpleSerializer extends JsonSerializer<Object> {
		@Override
		public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
			if (value instanceof Set<?> set) {
				serializeSet(set, gen);
			} else if (value instanceof Long longValue) {
				gen.writeString(IdEncrypter.encryptGenericId(longValue));
			}
		}

		private void serializeSet(Set<?> set, JsonGenerator gen) throws IOException {
			Set<Long> longs = (Set<Long>) set;
			String[] encrypted = longs.stream()
					.map(IdEncrypter::encryptGenericId)
					.distinct()
					.toArray(String[]::new);
			gen.writeStartArray();
			for (String value : encrypted) {
				gen.writeString(value);
			}
			gen.writeEndArray();
		}
	}
}


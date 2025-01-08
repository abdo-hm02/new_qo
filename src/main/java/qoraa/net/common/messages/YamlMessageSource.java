package qoraa.net.common.messages;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class YamlMessageSource extends AbstractMessageSource {

    private final Map<String, String> messages;

    public YamlMessageSource() throws IOException {
	this.messages = loadMessages("messages/messages.yml");
    }

	public Map<String, String> loadMessages(String yamlPath) throws IOException {
	    Resource resource = new ClassPathResource(yamlPath);
	    try (InputStream inputStream = resource.getInputStream()) {
		Yaml yaml = new Yaml();
		LinkedHashMap<String, Object> yamlData = yaml.load(inputStream);
		return flattenYamlMap(yamlData);
	    }
	}

	public Map<String, String> flattenYamlMap(Map<String, Object> yamlData) {
	    Map<String, String> flattenedMap = new HashMap<>();
	    flattenYamlMap("", yamlData, flattenedMap);
	    return flattenedMap;
	}

	@SuppressWarnings("unchecked")
	private void flattenYamlMap(String prefix, Map<String, Object> yamlData, Map<String, String> flattenedMap) {
	    yamlData.forEach((key, value) -> {
		if (value instanceof String valueString) {
		    flattenedMap.put(prefix + key, valueString);
		} else if (value instanceof Map) {
		    flattenYamlMap(prefix + key + ".", (Map<String, Object>) value, flattenedMap);
		}
	    });
	}

	@Override
	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
	    String message = messages.get(code);
	    if (message == null) {
		throw new NoSuchMessageException("Message not found with code: " + code);
	    }
	    return formatMessage(message, args);
	}

	@Override
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
	    return defaultMessage;
	}

	@Override
	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
	    throw new NoSuchMessageException("MessageSourceResolvable not supported by YamlMessageSource");
	}

	private String formatMessage(String message, Object[] args) {
	    return String.format(message, args);
	}
}

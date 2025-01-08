package qoraa.net.common.ticket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;
import java.util.stream.Stream;

@Slf4j
public class TicketArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
	return Ticket.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    @SuppressWarnings({"java:S3011","java:S135"})
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
	    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

	Ticket target = new Ticket();

	for (Field f : target.getClass().getDeclaredFields()) {
	    Ticket.Parameter annotation = f.getAnnotation(Ticket.Parameter.class);
	    if (annotation == null) {
		log.trace("Skipping field {}", f.getName());
		continue;
	    }
	    // bind field to a parameter name found in @Parameter annotation value
	    String paramName = Validate.notBlank(annotation.value(), "Param value must not be blank");
	    String[] paramValues = extractParamValues(webRequest, paramName, annotation.delimiter());
		if (paramValues == null || paramValues.length == 0) {
			log.trace("No param values for param {}", paramName);
			continue;
		}
	    Class<?> targetType = f.getType();
	    f.setAccessible(true);
	    f.set(target, convert(paramValues, targetType));

	}

	return target;

    }

    private Object convert(String[] paramValues, Class<?> targetType) {
	if (targetType.isAssignableFrom(Long.class)) {
	    return TicketConverter.fromDtoToInternal(paramValues[0], Long.class);
	}

	return null;

    }

    private String[] extractParamValues(NativeWebRequest webRequest, String paramName, String delimiter) {
	String[] values = webRequest.getParameterValues(paramName);
	if (values == null || values.length == 0) {
	    return new String[0];
	}
	Stream<String> stream = Stream.of(values).filter(StringUtils::isNotBlank);
	if (StringUtils.isEmpty(delimiter)) {
	    return stream.toArray(String[]::new);
	} else {
	    return stream.flatMap(s -> Stream.of(s.split(delimiter))).toArray(String[]::new);
	}
    }
}

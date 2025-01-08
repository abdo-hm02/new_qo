package qoraa.net.common.exception;

import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpStatus;
import qoraa.net.common.utils.MapUtils;

import java.io.Serial;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * {@link RuntimeException} that implements {@link ErrorResponse} to expose an
 * HTTP status, response headers, and a body formatted as an RFC 7808
 * {@link ProblemDetails}.
 *
 * <p>
 * The exception can be used as is, or it can be extended as a more specific
 * exception that populates the {@link ProblemDetails} type or detail fields, or
 * potentially adds other non-standard properties.
 */
public class ApplicationException extends RuntimeException implements ErrorResponse {
    public static final Supplier<ApplicationException> NOT_FOUND_EXCEPTION = () -> ApplicationException
	    .builder(HttpStatus.NOT_FOUND.value()).build();
    @Serial
    private static final long serialVersionUID = -6744617074408284383L;
    private final ProblemDetails body;
    private final Map<String, String> headers;
    private final int statusCode;
    public ApplicationException(Builder<?> builder) {
	statusCode = builder.statusCode;
	body = builder.body.build();
	headers = builder.headers;
    }

    public static Supplier<ApplicationException> notFoundExceptionWithMessage(String message) {
	return () -> ApplicationException.builder(HttpStatus.NOT_FOUND.value()).detail(message).build();
    }

    // *************************************************************
    // Life-cycle: constructing instance
    // *************************************************************

    /**
     * Static factory method used to create {@link Builder} instance with
     * {@code statusCode} which is required parameter.
     *
     * @param statusCode
     *            The HTTP status code
     * @return BaseExceptionBuilder instance
     */
    public static Builder<ApplicationExceptionBuilder> builder(int statusCode) {
	return new ApplicationExceptionBuilder(statusCode);
    }

    @Override
    public ProblemDetails getBody() {
	return body;
    }

    @Override
    public Map<String, String> getHeaders() {
	return headers;
    }

    @Override
    public String getMessage() {
	return statusCode + (MapUtils.isNotEmpty(headers) ? ", headers=" + headers : "") + ", " + body;
    }

    @Override
    public int getStatusCode() {
	return statusCode;
    }

    /**
     * Abstract {@code Builder}, suited well to class hierarchies. A form of the
     * builder pattern for {@link ApplicationException} that combines the safety of
     * the telescoping constructor pattern with the readability of the JavaBeans
     * pattern.
     * <p>
     * Instead of making the desired {@link ApplicationException} directly, the
     * application calls the {@link #builder(int statusCode)} method to get a
     * builder object. Then the client calls setter-like methods on the
     * {@code Builder} object to set each (optional) parameter of interest.
     * <p>
     * </p>
     * Finally, the client calls a parameterless {@link #build()} method to generate
     * the {@link ApplicationException}, which is immutable. The clients code will
     * be easy to write and, more importantly, easy to read.
     * </p>
     *
     * @author Nikola Bekcic
     * @since 1.0
     */
    public abstract static class Builder<T extends Builder<T>> {

	private final ProblemDetails.Builder body = ProblemDetails.builder();
	private Map<String, String> headers;
	private final int statusCode;

	protected Builder(final int statusCode) {
	    this.statusCode = statusCode;
	    HttpStatus httpStatus = HttpStatus.resolve(statusCode);
	    Validate.notNull(httpStatus, "'%d' is not valid status code".formatted(statusCode));
	    body.status(statusCode).title(httpStatus.getReasonPhrase());
	}

	public ApplicationException build() {
	    return new ApplicationException(this);
	}

	public T detail(String detail) {
	    body.detail(detail);
	    return getThis();
	}

	public T header(String name, String value) {
	    if (headers == null) {
		headers = new LinkedHashMap<>(1);
	    }
	    headers.put(name, value);
	    return getThis();
	}

	public T headers(Map<String, String> headers) {
	    if (MapUtils.isNotEmpty(headers)) {
		headers.forEach(this::header);
	    }
	    return getThis();
	}

	public T instance(URI instance) {
	    body.instance(instance);
	    return getThis();
	}

	public T properties(Map<String, Object> properties) {
	    body.properties(properties);
	    return getThis();
	}

	public T title(String title) {
	    Validate.notBlank(title, "'title' must not be blank");
	    body.title(title);
	    return getThis();
	}

	protected abstract T getThis();
    }

    /**
     * {@link Builder} implementation used to assembled {@link ApplicationException}
     * instance using fluent code style.
     *
     * @author Nikola Bekcic
     * @since 1.0
     */
    public static class ApplicationExceptionBuilder extends Builder<ApplicationExceptionBuilder> {

	public ApplicationExceptionBuilder(int statusCode) {
	    super(statusCode);
	}

	@Override
	protected ApplicationExceptionBuilder getThis() {
	    return this;
	}
    }
}


package qoraa.net.common.exception;


import java.util.Collections;
import java.util.Map;

/**
 * Representation of a complete <b>RFC 7807</b> error response including status,
 * headers, and an RFC 7808 formatted {@link ProblemDetails} body. Allows any
 * exception to expose HTTP error response information.
 *
 * <p>
 * {@link ApplicationException} is a default implementation of this interface
 * and a convenient base class for other application exceptions to use.
 * </p>
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7807">RFC 7807</a>
 */
public interface ErrorResponse {

    /**
     * Return the body for the response, formatted as an <b>RFC 7807</b>
     * {@link ProblemDetails}.
     */
    ProblemDetails getBody();

    /**
     * Return headers to use for the response.
     */
    default Map<String, String> getHeaders() {
	return Collections.emptyMap();
    }

    /**
     * Return the HTTP status code to use for the response.
     */
    int getStatusCode();
}

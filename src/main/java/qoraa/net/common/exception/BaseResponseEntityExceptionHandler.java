package qoraa.net.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import qoraa.net.common.utils.MapUtils;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ ApplicationException.class })
    protected ResponseEntity<ProblemDetails> handleApplicationException(ApplicationException e,
	    HttpServletRequest request) {
	log.error("Application exception while processing request: {}", toMethodAndUrl(request), e.getMessage());
	return new ResponseEntity<>(e.getBody(), toHttpHeaders(e.getHeaders()), e.getStatusCode());
    }

    protected HttpHeaders toHttpHeaders(Map<String, String> map) {
	if (MapUtils.isNullOrEmpty(map)) {
	    return new HttpHeaders();
	}
	HttpHeaders httpHeaders = new HttpHeaders();
	for (Map.Entry<String, String> header : map.entrySet()) {
	    httpHeaders.add(header.getKey(), header.getValue());
	}
	return httpHeaders;
    }

    protected String toMethodAndUrl(HttpServletRequest httpServletRequest) {
	if (httpServletRequest == null) {
	    return "N/A";
	}

	return httpServletRequest.getMethod() + " " + httpServletRequest.getRequestURL();
    }
}

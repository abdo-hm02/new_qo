package qoraa.net.common.exception;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.net.URI;
import java.util.Map;

@Builder(builderClassName = "Builder")
@Data
public class ProblemDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = 8299605968686889819L;
    public static final URI DEFAULTTYPE = URI.create("about:blank");

    String detail;
    URI instance;
    transient Map<String, Object> properties;
    Integer status;
    String title;
    URI type = DEFAULTTYPE;
}

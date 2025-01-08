package qoraa.net.common.openapi;

import io.swagger.v3.oas.models.servers.Server;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter(AccessLevel.PACKAGE)
@ConfigurationProperties(prefix = "info")
class OpenApiProperties {
    /**
     * The servers specify the API server and base URL. You can define one or
     * several servers, such as production and staging.
     */
    private final List<Server> servers = new ArrayList<>();

    @Setter
    private WebProperties web;

    /**
     * Title is the API name.
     */
    @Setter
    private String title;

    /**
     * Version is an arbitrary string that specifies the version of API (do not
     * confuse it with file revision or the openapi version)
     */
    @Setter
    private String version;

    @Setter
    @Getter(AccessLevel.PACKAGE)
    static class WebProperties {

        static final String WEB_API_GROUP_NAME = "Web API";

        static final String WEB_API_VERSION = "1.0";

        static final String WEB_API_PATH = "/webapi/**";

        /**
         * Title is the API name.
         */
        private String title;

    }
}

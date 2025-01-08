package qoraa.net.common.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
public class KeycloakProperties {
    private String realm;
    private String authServerUrl;
    private String resource;
    private Credentials credentials = new Credentials();

    @Getter
    @Setter
    public static class Credentials {
        private String secret;
    }
}



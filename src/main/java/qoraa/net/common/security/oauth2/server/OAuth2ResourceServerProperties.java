package qoraa.net.common.security.oauth2.server;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;

/**
 * OAuth 2.0 Resource Server properties.
 *
 */
@Getter
@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver")
class OAuth2ResourceServerProperties {

    private final JwtProperties jwt = new JwtProperties();

    @Getter
    @Setter
    public static class JwtProperties extends Jwt {
        private String audience;
        private String jwkSetUri;
    }
}


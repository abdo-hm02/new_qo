package qoraa.net.common.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak.admin")
@Getter
@Setter
public class KeycloakAdminProperties {
    private String realm = "master";
    private String clientId = "admin-cli";
    private String username;
    private String password;
}

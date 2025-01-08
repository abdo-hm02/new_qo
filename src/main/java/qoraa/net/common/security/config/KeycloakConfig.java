package qoraa.net.common.security.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {

    private final KeycloakProperties keycloakProperties;
    private final KeycloakAdminProperties adminProperties;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getAuthServerUrl())
                .realm(adminProperties.getRealm())
                .clientId(adminProperties.getClientId())
                .username(adminProperties.getUsername())
                .password(adminProperties.getPassword())
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}


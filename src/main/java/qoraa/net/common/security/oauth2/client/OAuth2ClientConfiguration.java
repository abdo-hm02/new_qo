package qoraa.net.common.security.oauth2.client;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import qoraa.net.common.security.config.KeycloakProperties;

@Configuration
@RequiredArgsConstructor
public class OAuth2ClientConfiguration {

    private final KeycloakProperties keycloakProperties;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration keycloakRegistration = ClientRegistration.withRegistrationId("keycloak")
                .clientId(keycloakProperties.getResource())
                .clientSecret(keycloakProperties.getCredentials().getSecret())
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .scope("openid", "profile", "email")
                .tokenUri(keycloakProperties.getAuthServerUrl() + "/realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/token")
                .build();

        return new InMemoryClientRegistrationRepository(keycloakRegistration);
    }
}


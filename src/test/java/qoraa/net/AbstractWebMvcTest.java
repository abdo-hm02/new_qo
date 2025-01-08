package qoraa.net;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import qoraa.net.common.messages.MessageSourceConfig;
import qoraa.net.common.security.authorization.UserAccountDetailsLoader;
import qoraa.net.common.security.config.KeycloakProperties;
import qoraa.net.common.security.config.SecurityConfig;
import qoraa.net.common.security.config.PropertiesConfig;

import static org.mockito.Mockito.mock;

@WebMvcTest
@Import({
        SecurityConfig.class,
        PropertiesConfig.class,
        MessageSourceConfig.class
})
@ActiveProfiles("test")
@ImportAutoConfiguration(exclude = OAuth2ClientAutoConfiguration.class)
@SpringJUnitConfig(classes = AbstractWebMvcTest.Configuration.class)
public abstract class AbstractWebMvcTest {

    @Autowired
    protected MockMvc mockMvc;

    @TestConfiguration
    static class Configuration {

        @Bean
        public JwtDecoder jwtDecoder() {
            return mock(JwtDecoder.class);
        }

        @Bean
        public UserAccountDetailsLoader userAccountDetailsLoader() {
            return mock(UserAccountDetailsLoader.class);
        }

        @Bean
        public KeycloakProperties keycloakProperties() {
            return mock(KeycloakProperties.class);
        }
    }
}


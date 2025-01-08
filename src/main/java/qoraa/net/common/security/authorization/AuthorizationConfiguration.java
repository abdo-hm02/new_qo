package qoraa.net.common.security.authorization;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
@EnableMethodSecurity
public class AuthorizationConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "qoraa.security.authorization", name = "check.user.status", havingValue = "true", matchIfMissing = true)
    AuthorizationManager<Object> authorizationManager(UserAccountDetailsLoader userAccountDetailsLoader) {
	return CompositeAuthorizationManager.builder()
		.authorizationManager(AuthenticatedAuthorizationManager.authenticated())
		.authorizationManager(new UserAccountStatusAuthorizationManager<>(userAccountDetailsLoader)).build();
    }

    @Bean
    Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter(
	    UserAccountDetailsLoader userAccountDetailsLoader) {
	return new JwtUserDetailsLoader(userAccountDetailsLoader);
    }

}

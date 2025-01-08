package qoraa.net.common.security.oauth2.server;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.List;
import java.util.function.Predicate;

import static org.springframework.security.oauth2.jwt.JwtClaimNames.AUD;


@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(OAuth2ResourceServerProperties.class)
class OAuth2ResourceServerConfiguration {

    private final OAuth2ResourceServerProperties oauth2ResourceServerProperties;

    private OAuth2TokenValidator<Jwt> audienceValidator() {
	String audience = oauth2ResourceServerProperties.getJwt().getAudience();
	Validate.notEmpty(audience, "'audience' must not be empty");
	Predicate<List<String>> predicate= aud -> aud.contains(audience);
	return new JwtClaimValidator<>(AUD,predicate );
    }

    @Bean
    JwtDecoder jwtDecoder() {
	String issuerUri = oauth2ResourceServerProperties.getJwt().getIssuerUri();

	NimbusJwtDecoder nimbusJwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);

	OAuth2TokenValidator<Jwt> issuerValidator = JwtValidators.createDefaultWithIssuer(issuerUri);
	var jwtValidator = new DelegatingOAuth2TokenValidator<>(issuerValidator, audienceValidator());

	nimbusJwtDecoder.setJwtValidator(jwtValidator);

	return nimbusJwtDecoder;
    }
}

package qoraa.net.common.security.authorization;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

@AllArgsConstructor
public class JwtUserDetailsLoader implements Converter<Jwt, AbstractAuthenticationToken> {

    private UserAccountDetailsLoader userDetailsLoader;

    /*@Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
	String email = jwt.getSubject();
	UserAccountDetails userDetails = userDetailsLoader.load(email);
	return new JwtUserDetailsAuthenticationToken(jwt, email, userDetails);
    }*/

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        UserAccountDetails userDetails = userDetailsLoader.load(email);
        return new JwtUserDetailsAuthenticationToken(jwt, email, userDetails);
    }
}

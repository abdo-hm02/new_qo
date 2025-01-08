package qoraa.net.common.security.authorization;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Collections;

@EqualsAndHashCode(callSuper = true)
@Getter
public class JwtUserDetailsAuthenticationToken extends JwtAuthenticationToken {

    private final UserDetails userDetails;

    public JwtUserDetailsAuthenticationToken(Jwt jwt, String name, UserDetails userDetails) {
	super(jwt, null, name);
	this.userDetails = userDetails;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (userDetails == null) {
            return Collections.emptyList();
        }
        return ((UserAccountDetails) userDetails).getAuthorities();
    }

}

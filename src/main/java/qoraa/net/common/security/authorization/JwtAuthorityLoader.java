package qoraa.net.common.security.authorization;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
class JwtAuthorityLoader implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final @NonNull PermissionLoader permissionLoader;

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
	Objects.requireNonNull(jwt);
	final String sub = jwt.getSubject();
	Collection<GrantedAuthority> authorities = Optional.ofNullable(permissionLoader.loadPermissions(sub))
		.orElse(Collections.emptySet()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
	log.debug("Loaded {} authorities for sub '{}'", authorities.size(), sub);
	return authorities;

    }

}

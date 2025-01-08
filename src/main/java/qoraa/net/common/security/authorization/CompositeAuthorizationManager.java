package qoraa.net.common.security.authorization;

import lombok.Builder;
import lombok.Singular;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.function.Supplier;

@Builder
public class CompositeAuthorizationManager<T> implements AuthorizationManager<T> {

    private static final AuthorizationDecision AUTHORIZATION_GRANTED = new AuthorizationDecision(true);
    @Singular
    private final Collection<AuthorizationManager<T>> authorizationManagers;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, T object) {
	if (CollectionUtils.isEmpty(authorizationManagers)) {
	    return null;
	}
	// iterate through all delegates until the first one denies access
	return authorizationManagers.stream().map(a -> a.check(authentication, object)).filter(this::isForbidden)
		.findFirst().orElse(AUTHORIZATION_GRANTED);
    }

    private boolean isForbidden(AuthorizationDecision authorizationDecision) {
	return !(authorizationDecision != null && authorizationDecision.isGranted());
    }
}

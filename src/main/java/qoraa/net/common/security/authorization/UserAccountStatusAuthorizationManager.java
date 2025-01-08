package qoraa.net.common.security.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Authorization manager which loads {@link UserDetails} using
 * {@link UserDetailsService} and check several flags to see if account is
 * disabled/locked/expired for any reason.
 */
@RequiredArgsConstructor
@Slf4j
public class UserAccountStatusAuthorizationManager<T> implements AuthorizationManager<T> {
    private static final AuthorizationDecision ACCESS_DENIED = new AuthorizationDecision(false);
    private static final AuthorizationDecision ACCESS_GRANTED = new AuthorizationDecision(true);
    private final UserDetailsChecker checker;
    private final UserAccountDetailsLoader userAccountDetailsLoader;

    UserAccountStatusAuthorizationManager(UserAccountDetailsLoader userDetailsService) {
	this(new AccountStatusUserDetailsChecker(), userDetailsService);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, T object) {
	final String userId = authentication.get().getName();

	var auth = authentication.get();

	UserDetails userDetails = (authentication instanceof JwtUserDetailsAuthenticationToken)
		? ((JwtUserDetailsAuthenticationToken) auth).getUserDetails()
		: userAccountDetailsLoader.load(userId);

	if (Objects.isNull(userDetails)) {
	    return ACCESS_DENIED;
	}
	try {
	    checker.check(userDetails);
	} catch (AccountStatusException e) {
	    log.trace("User account status check for {} failed", userId, e);
	    return ACCESS_DENIED;
	}
	return ACCESS_GRANTED;
    }

}

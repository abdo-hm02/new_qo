package qoraa.net.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import qoraa.net.common.security.authorization.JwtUserDetailsAuthenticationToken;

import static java.util.Objects.isNull;

@Slf4j
public final class AuthenticationUtils {
    private AuthenticationUtils() {
    }

    public static String getCurrentUserName() {
        JwtUserDetailsAuthenticationToken authentication = (JwtUserDetailsAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        String currentUsername = isNull(authentication) ? null : authentication.getUserDetails().getUsername();

        log.debug("Current  username is '{}'", currentUsername);
        return currentUsername;
    }
}

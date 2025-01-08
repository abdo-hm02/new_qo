package qoraa.net.common.auditing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static qoraa.net.common.utils.AuthenticationUtils.getCurrentUserName;

@Slf4j
class AuthenticatedUserAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
	String username = getCurrentUserName();
	log.debug("Returning  user '{}' as an auditor", username);
	return isEmpty(username) ? Optional.empty() : Optional.of(username);
    }
}

package qoraa.net.common.auditing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
    @Bean
    AuditorAware<String> authenticatedUserAuditorAware() {
        return new AuthenticatedUserAuditorAware();
    }
}

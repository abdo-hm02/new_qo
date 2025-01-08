package qoraa.net;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.test.context.ContextConfiguration;

import lombok.extern.slf4j.Slf4j;
import qoraa.net.common.auditing.JpaAuditingConfiguration;
import qoraa.net.common.security.oauth2.client.OAuth2ClientConfiguration;
import qoraa.net.common.security.config.KeycloakProperties;

@DataJpaTest
@Import({ JpaAuditingConfiguration.class, DefaultConversionService.class, OAuth2ClientConfiguration.class,
	OAuth2ClientProperties.class, KeycloakProperties.class })
@ContextConfiguration(classes = { AbstractPostgresRepositoryTest.PostgresIntegrationTestConfiguration.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public abstract class AbstractPostgresRepositoryTest implements PostgresTestContainerSupport {

    @Autowired
    protected TestEntityManager testEntityManager;

    protected void executeAndFlush(Runnable action) {
	action.run();
	log.info("Flushing persistence context to the database...");
	testEntityManager.flush();
	testEntityManager.clear();
    }

    @Configuration
    @EnableAutoConfiguration
    @ComponentScan(basePackages = {
		    "qoraa.net.modules",
		    "qoraa.net.common.service",
		    "qoraa.net.common.auditing"
    })
    static class PostgresIntegrationTestConfiguration {
    }
}

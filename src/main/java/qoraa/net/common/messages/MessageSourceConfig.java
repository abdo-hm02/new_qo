package qoraa.net.common.messages;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource() throws IOException {
	return new YamlMessageSource();
    }
}

package qoraa.net.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import qoraa.net.common.ticket.TicketArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Registers the TicketArgumentResolver to handle ticket parameters in controller methods.
     * This enables automatic conversion of encrypted ticket strings to Ticket objects,
     * providing a secure way to handle entity IDs in REST endpoints.
     *
     * The TicketArgumentResolver will:
     * 1. Intercept requests with ticket parameters
     * 2. Decrypt the ticket value using IdEncrypter
     * 3. Create a Ticket object with the decrypted ID
     * 4. Make it available as a method parameter in controllers
     */

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new TicketArgumentResolver());
    }
}


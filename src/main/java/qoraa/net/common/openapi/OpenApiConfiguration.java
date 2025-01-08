package qoraa.net.common.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static qoraa.net.common.openapi.OpenApiProperties.WebProperties.WEB_API_GROUP_NAME;
import static qoraa.net.common.openapi.OpenApiProperties.WebProperties.WEB_API_PATH;
import static qoraa.net.common.openapi.OpenApiProperties.WebProperties.WEB_API_VERSION;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OpenApiProperties.class)
public class OpenApiConfiguration {

    private final OpenApiProperties openApiProperties;

    @Value("${info.title}")
    private String title;

    @Value("${info.version}")
    private String version;

    @Value("${security.scheme}")
    private String scheme;

    @Value("${security.bearerFormat}")
    private String bearerFormat;

    @Bean
    public OpenAPI customOpenAPI() {
	return new OpenAPI().info(new Info().title(title).version(version))
		.components(new Components().addSecuritySchemes("Authorization",
			new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme(scheme).bearerFormat(bearerFormat)))
		.addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }

    @Bean
    GroupedOpenApi webApi() {
	return GroupedOpenApi.builder().group(WEB_API_GROUP_NAME).pathsToMatch(WEB_API_PATH)
		.addOpenApiCustomizer(api -> api.addSecurityItem(new SecurityRequirement().addList(AUTHORIZATION))
			.info(new Info().title(openApiProperties.getWeb().getTitle()).version(WEB_API_VERSION))
			.getComponents().addSecuritySchemes(AUTHORIZATION, securityScheme()))
		.build();
    }

    private SecurityScheme securityScheme() {
	return new SecurityScheme().in(SecurityScheme.In.HEADER).type(SecurityScheme.Type.HTTP).scheme("bearer")
		.bearerFormat("JWT");
    }

}

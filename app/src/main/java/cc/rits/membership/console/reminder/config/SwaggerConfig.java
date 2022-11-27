package cc.rits.membership.console.reminder.config;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cc.rits.membership.console.reminder.annotation.SwaggerHiddenParameter;
import cc.rits.membership.console.reminder.property.ProjectProperty;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;

/**
 * Swaggerの設定
 */
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(SwaggerHiddenParameter.class);
    }

    private final ProjectProperty projectProperty;

    @Bean
    public GroupedOpenApi frontApi() {
        return GroupedOpenApi.builder() //
            .group("Front API") //
            .packagesToScan("cc.rits.membership.console.reminder.infrastructure.api.controller.front") //
            .build();
    }

    @Bean
    public GroupedOpenApi batchApi() {
        return GroupedOpenApi.builder() //
            .group("Batch API") //
            .packagesToScan("cc.rits.membership.console.reminder.infrastructure.api.controller.batch") //
            .build();
    }

    @Bean
    public OpenAPI openAPI() {
        final var info = new Info() //
            .title("Reminder Internal API") //
            .version(this.projectProperty.getVersion());
        return new OpenAPI().info(info);
    }

}

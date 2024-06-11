package ${groupId}.${artifactId}.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * Required to autowire SpringSwaggerConfig
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
            .apis(RequestHandlerSelectors.basePackage("${groupId}.${artifactId}.controller"))
            .build()
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("${developerName}", "", "${developerEmailId}");

        ApiInfo apiInfo = new ApiInfo("${description}", "${name}", "V${version}", "https://www.gogle.com/terms-conditions", contact, "MIT License", "https://www.mit.com/privacy-policy");

        return apiInfo;
    }
}

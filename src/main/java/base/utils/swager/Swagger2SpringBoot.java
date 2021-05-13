package base.utils.swager;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
public class Swagger2SpringBoot {


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("base.web"))
                //.paths(PathSelectors.any())
                .paths(PathSelectors.regex("/student.*")
                        .or(PathSelectors.regex("/group.*"))
                        .or(PathSelectors.regex("/calc.*"))
                )
                .build()
                .pathMapping("/")
                //.securitySchemes(singletonList(apiKey()))
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "REST API",
                "REST description of API",
                "API 1.0",
                "Student microservece",
                new Contact("author", "https://...", "...@gmail.com"),
                "License of API", "API license URL", Collections.emptyList()
        );
    }

    private ApiKey apiKey() {
        return new ApiKey("ADMIN", "pswd", "header");
    }

}




package nl.rabobank.cdm.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    public static final String CUSTOMER_TAG = "Customer";
    public static final Contact DEFAULT_CONTACT = new Contact("Customer details support", "", "support@test.com");
    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Customer Details", "REST API for Customer Details",
            "1.0", null, DEFAULT_CONTACT, null, null, Collections.emptyList());

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("Customer Details").select()
                .apis(basePackage("nl.rabobank.cdm.controllers")).paths(PathSelectors.ant("/api/**")).build()
                .apiInfo(DEFAULT_API_INFO).useDefaultResponseMessages(false)
                .tags(new Tag(CUSTOMER_TAG, "Manage customer details"));
    }
}
package com.axiom.mobilehandset.swagger;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Configuration
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
@EnableSwagger2
public abstract class SwaggerConfiguration {
    private static final String API_DESCRIPTION_FILENAME = "api-doc/description.md";

    @Bean
    public Docket api() throws IOException {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        TypeResolver typeResolver = new TypeResolver();
        docket.alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(Collection.class, Instant.class),
                typeResolver.resolve(Collection.class, Date.class),
                Ordered.HIGHEST_PRECEDENCE));

        ApiSelectorBuilder selectorBuilder = docket.select();
        selectorBuilder.apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();

        docket.apiInfo(getApiInfo());

        docket.useDefaultResponseMessages(false);

        return docket;
    }

    private ApiInfo getApiInfo() throws IOException {
        String apiTitle = apiTitle();
        String apiDescription = getApiDescriptionFromFile();

        @SuppressWarnings("rawtypes")
        ApiInfo apiInfo = new ApiInfo(apiTitle,
                apiDescription,
                null,
                null,
                null,
                null,
                null,
                new ArrayList<VendorExtension>());
        return apiInfo;
    }

    private String getApiDescriptionFromFile() throws IOException {
        try (InputStream descriptionIs = this.getClass()
                .getClassLoader()
                .getResourceAsStream(API_DESCRIPTION_FILENAME)) {
            if (descriptionIs == null) {
                return null;
            }

            return new BufferedReader(
                    new InputStreamReader(descriptionIs, StandardCharsets.UTF_8)).lines()
                    .collect(Collectors.joining("\n"));
        }
    }

    public abstract String apiTitle();

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(0)
                .defaultModelExpandDepth(10)
                .defaultModelRendering(ModelRendering.MODEL)
                .displayRequestDuration(true)
                .docExpansion(DocExpansion.LIST)
                .filter(true)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(null)
                .validatorUrl("") // https://github.com/springfox/springfox/issues/2201
                .build();
    }
}
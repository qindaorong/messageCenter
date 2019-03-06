package com.xhxd.messagecenter.common.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@EnableSwagger2
@ComponentScan(basePackages = {"com.xhxd.messagecenter.web.controller"})
@Configuration
public class SwaggerConfig {

    private static boolean enable = true;

    @Bean
    public Docket createRestApi() {

        ParameterBuilder param = new ParameterBuilder();
        param.name("sessionId").description("sessionId").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        List<Parameter> aParameters = new ArrayList<>();

        aParameters.add(param.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xhxd.messagecenter.web.controller"))
                .paths(PathSelectors.any())
                .build().enable(enable)
                .globalOperationParameters(aParameters);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MessageCenter v1.0.0")
                .description("SMS服务")
                .termsOfServiceUrl("http://ip:port/swagger-ui.html")
                .contact("ycj")
                .version("1.0.0")
                .build();
    }
}
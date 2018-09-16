package com.basics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger 构建rest api 文档
 * http://127.0.0.1:8083/basics/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    //是否开启
    @Value(value = "${swagger.enabled}")
    Boolean swaggerEnabled;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户管理")
                .apiInfo(apiInfo())
                .enable(swaggerEnabled)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.test.database.controller"))
                .paths(PathSelectors.any()).build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring 中使用Swagger2构建文档")
                .termsOfServiceUrl("https://github.com/yongkanghuang/basics")
                .contact(new Contact("GitHub ", "https://github.com/yongkanghuang/basics", "770860791@qq.com"))
                .version("1.0").build();
    }
}

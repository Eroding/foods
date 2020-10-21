package com.cnh.config;

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
 * swagger2配置
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    // swagger2配置 docket
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)//指定api类型为swagger2
        .apiInfo(apiInfo())   //用于定义api文档汇总信息
        .select().apis(RequestHandlerSelectors.basePackage("com.cnh.controller"))
                .paths(PathSelectors.any())  //所有controller
                .build();


    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("这是一次架构之路，学习好多知识")
                .contact(new Contact("陈南海",
                        "https://www.baidu.com",
                        "1173672098@qq.com"))
                .description("专为此项目提供api")
                .version("1.0.1")
                .termsOfServiceUrl("https://www.baidu.com") //网站地址
                .build();


    }


}

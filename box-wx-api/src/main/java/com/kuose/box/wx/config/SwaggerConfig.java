package com.kuose.box.wx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 //注解启动swagger2
public class SwaggerConfig {
    @Bean
    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * select()
     * apis()函数返回一个ApiSelectorBuilder实例，指定接口暴露给swagger
     * paths
     * build()
     */
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kuose.box.wx.login.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 创建API基本信息（展现在文档页中）
     * title: 标题 description：文档描述
     * version:版本      contact:作者
     * termsOfServiceUrl:服务URL
     * @return
     */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("阔色盒子接口文档")
                .description("一个盒子系统接口文档的API")
                .version("1.0")
                .contact("方亚军")
                .termsOfServiceUrl("")
                .build();
    }
}
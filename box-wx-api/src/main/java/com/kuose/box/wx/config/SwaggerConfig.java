package com.kuose.box.wx.config;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    // 定义分隔符,配置Swagger多包
    private static final String splitor = ";";
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
                .apis(basePackage("com.kuose.box.wx.login.controller" +splitor+
                        "com.kuose.box.wx.order.controller" +splitor+
                        "com.kuose.box.wx.user.controller" +splitor+
                        "com.kuose.box.wx.express.controller" +splitor+
                        "com.kuose.box.wx.pay.controller" +splitor+
                        "com.kuose.box.wx.recommend.controller" +splitor+
                        "com.kuose.box.wx.test" +splitor+
                        "com.kuose.box.wx.survey.controller"
                ))
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
                .termsOfServiceUrl("")
                .build();
    }


    /**
     * 重写basePackage方法，使能够实现多包访问，复制贴上去
     * @author  teavamc
     * @date 2019/1/26
     * @param
     * @return com.google.common.base.Predicate<springfox.documentation.RequestHandler>
     */
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(splitor)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
}
package com.kuose.box.wx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@SpringBootApplication
@MapperScan(basePackages = "com.kuose.box.wx.*.dao")
public class BoxWxApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoxWxApiApplication.class, args);
    }

}

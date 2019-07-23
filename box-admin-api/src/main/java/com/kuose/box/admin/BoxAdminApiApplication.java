package com.kuose.box.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.kuose.box.admin.*.dao")
public class BoxAdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoxAdminApiApplication.class, args);
    }

}

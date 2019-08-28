package com.kuose.source.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author 魔舞清华
 */
@SpringBootApplication
@MapperScan(basePackages = "com.kuose.source.*.dao")
public class BoxGoodsSourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoxGoodsSourceApplication.class, args);
    }

}

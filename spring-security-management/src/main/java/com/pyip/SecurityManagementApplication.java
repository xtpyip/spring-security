package com.pyip;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName: SecurityManagementApplication
 * @version: 1.0
 * @Author: pyipXt
 * @Description: 启动类
 **/
@SpringBootApplication
@MapperScan(basePackages = {"com.pyip.mapper"}) // mybatis包扫描
public class SecurityManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityManagementApplication.class, args);
    }

}

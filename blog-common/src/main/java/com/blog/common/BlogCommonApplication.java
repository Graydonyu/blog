package com.blog.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class BlogCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogCommonApplication.class, args);
    }

}

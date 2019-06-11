package com.blog.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class BlogImApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogImApplication.class, args);
    }

}

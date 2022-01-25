package com.springbootsecuritybasic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // BaseEntity 를 이용하기 위해 추가한 annotation.
public class SpringBootSecurityBasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityBasicApplication.class, args);
    }

}

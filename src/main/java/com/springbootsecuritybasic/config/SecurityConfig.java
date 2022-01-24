package com.springbootsecuritybasic.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 비밀번호 암호화를 위한 PasswordEncoder. Boot 2.0 이상부터 반드시 사용해야 한다.
    @Bean
    PasswordEncoder passwordEncoder(){
        // BCryptPasswordEncoder 는 bcrypt 라는 해시 함수로 패스워드를 암호화한다.
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication().withUser("user1")
                .password("$2a$10$Cdvv0/zRe01SZN9U1fVXw.GMld3k8cCRTzUzYQIaqhxeWEJF6rtq6") // test 에서 1111을 인코딩한 결과.
                .roles("USER");

    }
}

package com.springbootsecuritybasic.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
    protected void configure(HttpSecurity http) throws Exception {
        // 1) .antMatchers().permitAll() : 해당 페이지는 로그인 없이 모든 사용자에게 개방.
        // 정확히는 로그인이 없더라도 익명의 사용자로 취급하여 페이지를 개방함.
        // 2) .antMatchers().hasRole("user의 권한명") : 권한명과 일치하는 권한을 가진 유저만 접근가능.
        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/member").hasRole("USER");

        http.formLogin(); // 3) 인증에 문제가 있을 경우 login 페이지로 이동.
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication().withUser("user1") // 작성할 user id.
                .password("$2a$10$Cdvv0/zRe01SZN9U1fVXw.GMld3k8cCRTzUzYQIaqhxeWEJF6rtq6") // test 에서 1111을 인코딩한 결과.
                .roles("USER"); // 이 아이디의 권한.
    }


}

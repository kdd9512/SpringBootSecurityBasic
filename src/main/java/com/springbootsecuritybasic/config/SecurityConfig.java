package com.springbootsecuritybasic.config;

import com.springbootsecuritybasic.security.filter.ApiCheckFilter;
import com.springbootsecuritybasic.security.filter.ApiLoginFilter;
import com.springbootsecuritybasic.security.handler.ApiLoginFailHandler;
import com.springbootsecuritybasic.security.handler.LoginSuccessHandler;
import com.springbootsecuritybasic.security.service.SecUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Log4j2
// 어노테이션 기반의 접근제한을 설정.
// securedEnabled : 이전 버전의 @Secure annotation 의 사용가능 여부 설정
// prePostEnabled : @PreAuthorize annotation 의 사용가능 여부 결정.
// @PreAuthorize 는 controller 에 annotation 을 직접 설정하여 접근제한 조건을 부여한다.
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecUserDetailsService userDetailsService;

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
//        http.authorizeRequests()
//                .antMatchers("/sample/all").permitAll()
//                .antMatchers("/sample/member").hasRole("USER");

        http.formLogin(); // 3) 인증에 문제가 있을 경우 login 페이지로 이동.

        // csrf 토큰 해제.
        // 사용자 요청을 위조하여 특정 페이지의 보안을 무력화 시키는 것을 방지하기 위해 임의의 값인 csrf 토큰을 만들어 발행한다.
        // form 태그에는 보안상 권장되나, REST 방식에서 매번 이 값을 알아야 하기 때문에 불편할 수 있다.
        http.csrf().disable();

        // 기본 제공되는 logout 페이지.
        http.logout();

        // oauth2 를 이용하여 login. google 로그인을 이용한다.
        http.oauth2Login().successHandler(successHandler());

        // 자동로그인 지정. 지정한 기간은 1주일.
        http.rememberMe().tokenValiditySeconds(60*60*7).userDetailsService(userDetailsService);

        // Filter 는 보통 가장 마지막에 동작하나, 이하의 addFilterBefore 로 순서를 조절할 수 있다.
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);


    }

    @Bean
    public LoginSuccessHandler successHandler() {
        return new LoginSuccessHandler(passwordEncoder());
    }

    // SecAuthMemberDTO 와 SecUserDetailService 가 이하 AuthenticationManagerBuilder 의 역할을 대신하므로 더 이상 필요없다.
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth.inMemoryAuthentication().withUser("user1") // 작성할 user id.
//                .password("$2a$10$Cdvv0/zRe01SZN9U1fVXw.GMld3k8cCRTzUzYQIaqhxeWEJF6rtq6") // test 에서 1111을 인코딩한 결과.
//                .roles("USER"); // 이 아이디의 권한.
//    }

    // API 서버를 위한 filter.
    @Bean
    public ApiCheckFilter apiCheckFilter(){
        return new ApiCheckFilter("/notes/**/*");
    }

    // 로그인 여부를 감지하는 filter.
    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception{

        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login");
        apiLoginFilter.setAuthenticationManager(authenticationManager());

        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler()); // login 실패 처리.

        return apiLoginFilter;
    }

}

package com.springbootsecuritybasic.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;

    public ApiCheckFilter(String pattern) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("REQUESTED URL : " + req.getRequestURI());

        log.info(antPathMatcher.match(pattern, req.getRequestURI()));

        if (antPathMatcher.match(pattern, req.getRequestURI())) {
            log.info("ApiCheckFilter................................................................");
            log.info("ApiCheckFilter................................................................");
            log.info("ApiCheckFilter................................................................");

            return;
        }
        filterChain.doFilter(req, resp);
    }
}

package com.springbootsecuritybasic.security.filter;

import com.springbootsecuritybasic.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
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

            boolean checkHeader = checkAuthHeader(req);

            if (checkHeader) {
                filterChain.doFilter(req, resp);
                return;
            } else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.setContentType("application/json;charset=utf-8"); // json 리턴, 한글깨짐 수정.
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = resp.getWriter();
                out.print(json);
                return;
            }

        }
        filterChain.doFilter(req, resp);
    }

    private boolean checkAuthHeader(HttpServletRequest req){

        boolean checkResult = false;

        String authHeader = req.getHeader("Authorization");

        // Authorization 의 헤더는 일반적으로 Basic 을 사용되나, JWT 를 이용하는 경우, Bearer 를 사용한다.
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            log.info("Authorization exist? : " + authHeader);

            try {
                String email = jwtUtil.validateAndExtract(authHeader.substring(7));
                log.info("validate result : " + email);
                checkResult = email.length() > 0;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return checkResult;
    }

}

package com.springbootsecuritybasic.security.filter;

import com.springbootsecuritybasic.security.dto.SecAuthMemberDTO;
import com.springbootsecuritybasic.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessUrl, JWTUtil jwtUtil) {
        super(defaultFilterProcessUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp)
            throws AuthenticationException, IOException, ServletException {
        log.info("==========================ApiLoginFilter==========================");

        String email = req.getParameter("email");
        String pw = req.getParameter("pw");

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, pw);

        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse resp,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("==========================ApiLoginFilter==========================");
        log.info("successfulAuthentication : " + authResult);

        log.info(authResult.getPrincipal());

        // email
        String email = ((SecAuthMemberDTO)authResult.getPrincipal()).getUsername();

        String token = null;
        try {
            token = jwtUtil.generateToken(email);

            resp.setContentType("text/plain");
            resp.getOutputStream().write(token.getBytes());

            log.info(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

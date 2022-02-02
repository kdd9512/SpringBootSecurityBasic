package com.springbootsecuritybasic.security.handler;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiLoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.info("login fail handler.....................");
        log.info(exception.getMessage());

        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.setContentType("application/json;charset=utf-8"); // json 리턴.

        JSONObject json = new JSONObject();
        String message = exception.getMessage();
        json.put("code", "401");
        json.put("message", message);

        PrintWriter out = resp.getWriter();
        out.print(json);
    }
}

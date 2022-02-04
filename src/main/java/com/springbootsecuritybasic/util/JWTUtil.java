package com.springbootsecuritybasic.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    private String secretKey = "kdd12345678";

    private long expire = 60 * 24 * 30;

    public String generateToken(String content) throws Exception {

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now()
                        .plusMinutes(expire).toInstant())) // JWT 만료기한 설정.
//                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(1).toInstant())) // JWT 만료기한을 1초로 설정.
                .claim("sub", content)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();

    }

    public String validateAndExtract(String tokenStr) throws Exception {

        String contentVal = null;

        try {
            DefaultJws defaultJws = (DefaultJws) Jwts.parser()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(tokenStr);

            log.info("defaultJws : " + defaultJws);
            log.info(defaultJws.getBody().getClass());

            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();

            log.info("=========================================================");

            contentVal = claims.getSubject();

        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            contentVal = null;
        }
        return contentVal;

    }

}

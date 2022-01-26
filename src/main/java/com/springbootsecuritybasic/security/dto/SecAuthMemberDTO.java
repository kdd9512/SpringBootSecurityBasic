package com.springbootsecuritybasic.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log4j2
@Getter
@Setter
@ToString
public class SecAuthMemberDTO extends User {

    private String email;
    private String name;
    private boolean fromSocial;

    public SecAuthMemberDTO(
            String username,
            String password,
            boolean fromSocial,
            Collection<?extends GrantedAuthority> auth) {

        super(username, password, auth);
        this.email = username;
        this.fromSocial = fromSocial;
    }

}

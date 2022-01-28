package com.springbootsecuritybasic.security.service;

import com.springbootsecuritybasic.entity.SecMemberEntity;
import com.springbootsecuritybasic.entity.SecMemberRoleEntity;
import com.springbootsecuritybasic.repository.SecMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class SecOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final SecMemberRepository repository;
    private final PasswordEncoder pwEncoder;

    private SecMemberEntity saveSocialMember(String email){
        // 기존에 동일 email 로 가입한 회원이 있는 경우에는 조회만 실시.
        Optional<SecMemberEntity> result = repository.findByEmail(email,true);

        if (result.isPresent()) {
            return result.get();
        }

        // 없는 경우, 이름을 email 로 / 비밀번호 1111 로 신규회원 가입.
        SecMemberEntity member = SecMemberEntity.builder().email(email)
                .name(email)
                .password(pwEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        member.addMemberRole(SecMemberRoleEntity.USER);

        repository.save(member);

        return member;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("==================================================");
        log.info("OAuth2User userRequest : " + userRequest); // org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest 객체.

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("OAuth2User clientName : " + clientName);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("==================================================");
        oAuth2User.getAttributes().forEach((key, val) -> {
            log.info(key + ":" + val);
        });

        String email = null;

        if (clientName.equals("Google")) { // Google 을 이용하는 경우.
            email = oAuth2User.getAttribute("email");
        }

        log.info("user email : " + email);

        SecMemberEntity member = saveSocialMember(email);

        return oAuth2User;

    }
}

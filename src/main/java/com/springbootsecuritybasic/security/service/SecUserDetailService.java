package com.springbootsecuritybasic.security.service;

import com.springbootsecuritybasic.entity.SecMemberEntity;
import com.springbootsecuritybasic.repository.SecMemberRepository;
import com.springbootsecuritybasic.security.dto.SecAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service // 자동으로 spring 에서 bean 으로 처리되게 하기 위한 annotation.
@RequiredArgsConstructor // 초기화되지 않은 final 필드나 @NonNull 필드에 생성자를 생성. 의존성 주입을 하려면 이하의 final 처리와 함께 필수.
public class SecUserDetailService implements UserDetailsService {

    private final SecMemberRepository memberRepository; // SecMemberRepository 를 주입받음. final 필수.

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("SecUserDetailService loadUserByUsername : " + username);

        // username 은 SecMemberEntity 의 email 을 의미하므로 repository 의 findByEmail 을 호출.
        Optional<SecMemberEntity> result = memberRepository.findByEmail(username, false);

        // 예외처리. 존재하지 않는 사용자를 입력하였을 때 이하의 error 를 throw.
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Check email address or social");
        }

        SecMemberEntity member = result.get();

        log.info("======================================================");
        log.info(member);

        // SecMemberEntity 를 UserDetail 타입으로 처리하기 위해 SecAuthMemberDTO 타입으로 변환한다.
        SecAuthMemberDTO authMember = new SecAuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.isFromSocial(),
                // 스프링 Security 에서 사용하는 SimpleGrantedAuthority 로 변환하고,
                // 기존 ROLE 에(SecMemberRoleEntity 참고) 접두어를 추가하여 사용.
                member.getRoleSet().stream()
                        .map(role ->
                                new SimpleGrantedAuthority("ROLE_" + role.name())
                        ).collect(Collectors.toList())
        );

        authMember.setName(member.getName());
        authMember.setFromSocial(member.isFromSocial());

        return authMember;
    }
}

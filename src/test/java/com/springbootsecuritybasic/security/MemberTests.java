package com.springbootsecuritybasic.security;


import com.springbootsecuritybasic.entity.SecMemberEntity;
import com.springbootsecuritybasic.entity.SecMemberRoleEntity;
import com.springbootsecuritybasic.repository.SecMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberTests {

    @Autowired
    private SecMemberRepository repository;
    @Autowired
    private PasswordEncoder pwEncoder;

    @Test
    public void insertDummyData(){
        IntStream.rangeClosed(1,100).forEach(i -> {
            SecMemberEntity member = SecMemberEntity.builder()
                    .email("user" + i + "@email.com")
                    .name("사용자" + i)
                    .fromSocial(false)
                    .password(pwEncoder.encode("1111")) // 비밀번호는 1111로 통일하되 passwordEncoder 이용하여 복호화한다.
                    .build();

            member.addMemberRole(SecMemberRoleEntity.USER); // 모든 회원에 일반 user(=USER) 권한부여.

            if (i > 80) {
                // 80 번 이상의 id 부터 MANAGER 권한 부여.
                member.addMemberRole(SecMemberRoleEntity.MANAGER);
            }

            if (i > 90) {
                // 90 번 이상의 id 부터 ADMIN 권한 부여.
                member.addMemberRole(SecMemberRoleEntity.ADMIN);
            }

            // 위 조건을 토대로 만든 data 들을 repository 에 저장 후 DB 에 제출.
            repository.save(member);

        });
    }

    @Test
    public void testRead(){

        Optional<SecMemberEntity> result = repository.findByEmail("user99@email.com", false);

        SecMemberEntity member = result.get();

        System.out.println(member);

    }

}

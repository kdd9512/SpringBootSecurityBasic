package com.springbootsecuritybasic.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

// DB 상에 이하의 조건을 갖는 테이블이 존재하지 않는 경우 프로젝트 실행 시 생성한다.
@Entity
// 이하 3개는 세트.
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Getter
@ToString
public class SecMemberEntity extends BaseEntity {

    @Id
    private String email;

    private String password;
    private String name;
    private boolean fromSocial;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SecMemberRoleEntity> roleSet = new HashSet<>();

    public void addMemberRole(SecMemberRoleEntity role) {
        roleSet.add(role);
    }

}

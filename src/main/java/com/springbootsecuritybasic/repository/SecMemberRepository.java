package com.springbootsecuritybasic.repository;

import com.springbootsecuritybasic.entity.SecMemberEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SecMemberRepository extends JpaRepository<SecMemberEntity, String> {

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from SecMemberEntity m where m.fromSocial = :social and m.email = :email")
    Optional<SecMemberEntity> findByEmail(String email, boolean social);
}

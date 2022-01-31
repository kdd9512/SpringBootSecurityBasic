package com.springbootsecuritybasic.repository;

import com.springbootsecuritybasic.entity.NoteEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<NoteEntity, Long> {

    @EntityGraph(attributePaths = "writer", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select n from NoteEntity n where n.num = :num")
    Optional<NoteEntity> getWithWriter(Long num);

    @EntityGraph(attributePaths = {"writer"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select n from NoteEntity n where n.writer.email = :email")
    List<NoteEntity> getList(String email);

}

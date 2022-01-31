package com.springbootsecuritybasic.service;

import com.springbootsecuritybasic.dto.NoteDTO;
import com.springbootsecuritybasic.entity.NoteEntity;
import com.springbootsecuritybasic.entity.SecMemberEntity;

import java.util.List;

public interface NoteService {

    Long register(NoteDTO noteDTO);

    NoteDTO get(Long num);

    void modify(NoteDTO noteDTO);

    void remove(Long num);

    List<NoteDTO> getAllWithWriter(String writerEmail);

    default NoteEntity dtoToEntity(NoteDTO noteDTO) {
        NoteEntity noteEntity = NoteEntity.builder()
                .num(noteDTO.getNum())
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .writer(SecMemberEntity.builder().email(
                        noteDTO.getWriterEmail()).build())
                .build();

        return noteEntity;
    }

    default NoteDTO entityToDTO(NoteEntity note) {

        NoteDTO noteDTO = NoteDTO.builder()
                .num(note.getNum())
                .title(note.getTitle())
                .content(note.getContent())
                .writerEmail(note.getWriter().getEmail())
                .regDate(note.getRegDate())
                .modDate(note.getModDate())
                .build();

        return noteDTO;
    }

}

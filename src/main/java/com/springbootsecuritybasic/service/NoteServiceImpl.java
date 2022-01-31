package com.springbootsecuritybasic.service;

import com.springbootsecuritybasic.dto.NoteDTO;
import com.springbootsecuritybasic.entity.NoteEntity;
import com.springbootsecuritybasic.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public Long register(NoteDTO noteDTO) {
        NoteEntity note = dtoToEntity(noteDTO);

        log.info("==================================================");
        log.info("NoteEntity : " + note);

        noteRepository.save(note);

        return note.getNum();
    }

    @Override
    public NoteDTO get(Long num) {

        Optional<NoteEntity> result = noteRepository.getWithWriter(num);

        // 이하 return 과 동일한 코드.
//        if (result.isPresent()) {
//            return entityToDTO(result.get());
//        }
//        return null;

        return result.map(this::entityToDTO).orElse(null);

    }

    @Override
    public void modify(NoteDTO noteDTO) {

        Long num = noteDTO.getNum();

        Optional<NoteEntity> result = noteRepository.findById(num);

        if (result.isPresent()){
            NoteEntity note = result.get();
            note.changeTitle(noteDTO.getTitle());
            note.changeContent(noteDTO.getContent());

            noteRepository.save(note);
        }

    }

    @Override
    public void remove(Long num) {

        noteRepository.deleteById(num);

    }

    @Override
    public List<NoteDTO> getAllWithWriter(String writerEmail) {

        List<NoteEntity> noteList = noteRepository.getList(writerEmail);

        // .map(this::entityToDTO) 는, .map(note -> entityToDTO(note)) 와 동일코드.
        return noteList.stream().map(this::entityToDTO).collect(Collectors.toList());
    }
}

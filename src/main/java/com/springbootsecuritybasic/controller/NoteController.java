package com.springbootsecuritybasic.controller;

import com.springbootsecuritybasic.dto.NoteDTO;
import com.springbootsecuritybasic.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/notes/")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService service; // final

    @PostMapping(value = "")
    public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO) {

        log.info("=============================register==================================");
        log.info("NoteController NoteDTO : " + noteDTO);

        Long num = service.register(noteDTO);

        return new ResponseEntity<>(num, HttpStatus.OK);
    }

}

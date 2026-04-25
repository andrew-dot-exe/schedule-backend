package ru.andrewexe.schedule.controller;

import org.springframework.web.bind.annotation.*;
import ru.andrewexe.schedule.dto.subject.AddSubjectRequestDto;
import ru.andrewexe.schedule.dto.subject.SubjectRequestDto;
import ru.andrewexe.schedule.dto.subject.SubjectResponseDto;
import ru.andrewexe.schedule.service.SubjectService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subject")
public class SubjectController {
    private final SubjectService service;

    public SubjectController(SubjectService service) {
        this.service = service;
    }

    @GetMapping("/get")
    public List<SubjectResponseDto> getAllSubjects()
    {
        return service.getSubjects();
    }

    @GetMapping("/get/{id}")
    public SubjectResponseDto getSubjectById(@PathVariable("id") Long id){
        return service.getSubjectById(id);
    }

    @PostMapping("/add")
    public SubjectResponseDto addSubject( @RequestBody AddSubjectRequestDto requestDto){
        return service.createSubject(requestDto);
    }

    @DeleteMapping("/remove")
    public SubjectResponseDto removeDto(@RequestBody SubjectRequestDto requestDto){
        return service.deleteSubject(requestDto);
    }

}

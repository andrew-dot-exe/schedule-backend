package ru.andrewexe.schedule.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.andrewexe.schedule.dto.teacher.TeacherRequestDto;
import ru.andrewexe.schedule.dto.teacher.TeacherResponseDto;
import ru.andrewexe.schedule.service.TeacherService;

import java.util.List;

@RestController(value = "/api/v1")
@Tag(name = "Контроллер преподавателей", description = "Контроллер, отвечающий за работу с преподавателями.")
public class TeacherController {

    @Autowired
    private TeacherService service;

    public TeacherController(TeacherService service) {
        this.service = service;
    }

    @GetMapping("/teacher/get")
    public List<TeacherResponseDto> getAllTeachers(){
        return service.listTeachers();
    }

    @GetMapping("/teacher/{id}")
    public TeacherResponseDto getTeacherById(@PathVariable("id") Long id){
        return service.getTeacherById(id);
    }

    @PostMapping("/teacher/create")
    public TeacherResponseDto createTeacher(@RequestBody TeacherRequestDto requestDto)
    {
        return service.createTeacher(requestDto);
    }

    @DeleteMapping("/teacher/delete/")
    public TeacherResponseDto deleteTeacher(@RequestBody TeacherRequestDto requestDto){
        return service.deleteTeacher(requestDto);
    }

    @PatchMapping("/teacher/update")
    public TeacherResponseDto updateTeacher(@RequestBody TeacherRequestDto requestDto){
        return service.updateTeacher(requestDto);
    }
}

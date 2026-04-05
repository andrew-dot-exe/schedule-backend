package ru.andrewexe.schedule.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.andrewexe.schedule.dto.teacher.TeacherResponseDto;
import ru.andrewexe.schedule.dto.teacher.TeacherRequestDto;
import ru.andrewexe.schedule.entity.Teacher;
import ru.andrewexe.schedule.repository.TeacherRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    private TeacherRepository repository;

    @Autowired
    public TeacherService(TeacherRepository repository) {
        this.repository = repository;
    }

    public List<TeacherResponseDto> listTeachers(){
        return repository.findAll().stream().map(
                teacher -> new TeacherResponseDto(teacher.getId(), teacher.getAcademicDegree(),
                        teacher.getFullName(), teacher.getPhoneNumber(), teacher.getVkId())
        ).toList();
    }

    public TeacherResponseDto getTeacherById(Long id){
        Optional<Teacher> teacher = repository.findById(id);
        if(teacher.isPresent()){
            Teacher tch = teacher.get();
            return new TeacherResponseDto(
                    tch.getId(),
                    tch.getAcademicDegree(),
                    tch.getFullName(),
                    tch.getPhoneNumber(),
                    tch.getVkId()
            );
        }
        return null;
    }


    public TeacherResponseDto createTeacher(TeacherRequestDto requestDto){
        Teacher teacher = new Teacher();
        teacher.setAcademicDegree(requestDto.academicDegree());
        teacher.setFullName(requestDto.fullName());
        teacher.setPhoneNumber(requestDto.phoneNumber());
        teacher.setVkId(requestDto.vkId());
        repository.save(teacher);
        return null;
    }

    public TeacherResponseDto updateTeacher(TeacherRequestDto requestDto){
        return createTeacher(requestDto);
    }

    public TeacherResponseDto deleteTeacher(TeacherRequestDto requestDto){
        Optional<Teacher> teacherOpt = repository.findById(requestDto.Id());
        if(teacherOpt.isPresent()){
            Teacher toDelete = teacherOpt.get();
            repository.delete(toDelete);
            return new TeacherResponseDto(
                    toDelete.getId(),
                    toDelete.getAcademicDegree(),
                    toDelete.getFullName(),
                    toDelete.getPhoneNumber(),
                    toDelete.getVkId()
            );
        }
        return null;
    }

}


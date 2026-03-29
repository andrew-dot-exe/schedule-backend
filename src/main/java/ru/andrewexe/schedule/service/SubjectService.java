package ru.andrewexe.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import ru.andrewexe.schedule.dto.SubjectRequestDto;
import ru.andrewexe.schedule.dto.SubjectResponseDto;
import ru.andrewexe.schedule.entity.Subject;
import ru.andrewexe.schedule.entity.Teacher;
import ru.andrewexe.schedule.repository.SubjectRepository;

import java.util.List;
import java.util.Set;

@Service
public class SubjectService {

    private SubjectRepository repository;

    @Autowired
    public SubjectService(SubjectRepository repository){
        this.repository  = repository;
    }

    public List<SubjectResponseDto> GetSubjects(){
        return repository.findAll().stream().map(
                subject -> new SubjectResponseDto(subject.getId(), subject.getName())
        ).toList();
    }

    public SubjectResponseDto findOneSubject(SubjectRequestDto requestDto){
        Subject sbj = repository.findFirstByName(requestDto.name());
        SubjectResponseDto responseDto = new SubjectResponseDto(
                sbj.getId(), sbj.getName()
        );
        return responseDto;
    }

    public SubjectResponseDto createSubject(SubjectRequestDto requestDto){
        Subject subject = new Subject();
        subject.setName(requestDto.name());
        if (requestDto != null) {
            subject.setTeachers(requestDto.teachers());
        }
        repository.save(subject);
        Subject sbj = repository.findFirstByName(requestDto.name());
        SubjectResponseDto responseDto = new SubjectResponseDto(
                sbj.getId(), sbj.getName()
        );
        return responseDto;
    }

    public SubjectResponseDto updateSubject(SubjectRequestDto requestDto){
        // update is the same as inserting
        return createSubject(requestDto);
    }

    public SubjectResponseDto deleteSubject(SubjectRequestDto requestDto){
        Subject sbj = repository.findFirstByName(requestDto.name());
        repository.delete(sbj);
        SubjectResponseDto responseDto = new SubjectResponseDto(
                sbj.getId(), sbj.getName()
        );
        return responseDto;
    }
}

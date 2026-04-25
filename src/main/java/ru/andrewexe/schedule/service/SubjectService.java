package ru.andrewexe.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.andrewexe.schedule.dto.subject.AddSubjectRequestDto;
import ru.andrewexe.schedule.dto.subject.SubjectRequestDto;
import ru.andrewexe.schedule.dto.subject.SubjectResponseDto;
import ru.andrewexe.schedule.entity.Subject;
import ru.andrewexe.schedule.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private SubjectRepository repository;

    @Autowired
    public SubjectService(SubjectRepository repository){
        this.repository  = repository;
    }

    public List<SubjectResponseDto> getSubjects(){
        return repository.findAll().stream().map(
                subject -> new SubjectResponseDto(subject.getId(), subject.getName())
        ).toList();
    }

    public SubjectResponseDto getSubjectById(Long id)
    {
        Optional<Subject> subject = repository.findById(id);
        if(subject.isPresent()){
            return new SubjectResponseDto(
                    subject.get().getId(),
                    subject.get().getName()
            );
        }
        return null;
    }

    public SubjectResponseDto findOneSubject(SubjectRequestDto requestDto){
        Subject sbj = repository.findFirstByName(requestDto.name());
        SubjectResponseDto responseDto = new SubjectResponseDto(
                sbj.getId(), sbj.getName()
        );
        return responseDto;
    }

    public SubjectResponseDto createSubject(AddSubjectRequestDto requestDto){
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

    public SubjectResponseDto updateSubject(AddSubjectRequestDto requestDto){
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

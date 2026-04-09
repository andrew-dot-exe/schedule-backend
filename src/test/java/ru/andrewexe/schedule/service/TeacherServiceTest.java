package ru.andrewexe.schedule.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.andrewexe.schedule.dto.teacher.TeacherRequestDto;
import ru.andrewexe.schedule.dto.teacher.TeacherResponseDto;
import ru.andrewexe.schedule.entity.Teacher;
import ru.andrewexe.schedule.repository.TeacherRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository repository;

    @InjectMocks
    private TeacherService service;

    @Test
    void listTeachersMapsRepositoryEntitiesToDtos() {
        when(repository.findAll()).thenReturn(List.of(
                teacher(1L, "Professor", "Alice Smith", "+79990000001", "@alice"),
                teacher(2L, "Assistant", "Bob Jones", null, null)
        ));

        List<TeacherResponseDto> result = service.listTeachers();

        assertThat(result)
                .extracting(TeacherResponseDto::Id, TeacherResponseDto::academicDegree, TeacherResponseDto::fullName)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple(1L, "Professor", "Alice Smith"),
                        org.assertj.core.groups.Tuple.tuple(2L, "Assistant", "Bob Jones")
                );
    }

    @Test
    void createTeacherSavesRequestAndReturnsSavedTeacher() {
        TeacherRequestDto request = new TeacherRequestDto(null, "Professor", "Alice Smith",
                "+79990000001", "@alice");
        when(repository.save(argThat(matchesTeacher(null, "Professor", "Alice Smith",
                "+79990000001", "@alice"))))
                .thenAnswer(invocation -> {
                    Teacher teacher = invocation.getArgument(0);
                    teacher.setId(10L);
                    return teacher;
                });

        TeacherResponseDto result = service.createTeacher(request);

        assertThat(result).isEqualTo(new TeacherResponseDto(10L, "Professor", "Alice Smith",
                "+79990000001", "@alice"));
    }

    @Test
    void updateTeacherPreservesTeacherIdWhenSaving() {
        TeacherRequestDto request = new TeacherRequestDto(7L, "Docent", "Jane Doe",
                "+79991112233", "@jane");
        when(repository.save(argThat(matchesTeacher(7L, "Docent", "Jane Doe",
                "+79991112233", "@jane"))))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TeacherResponseDto result = service.updateTeacher(request);

        assertThat(result).isEqualTo(new TeacherResponseDto(7L, "Docent", "Jane Doe",
                "+79991112233", "@jane"));
    }

    @Test
    void getTeacherByIdReturnsDtoWhenTeacherExists() {
        when(repository.findById(3L)).thenReturn(Optional.of(
                teacher(3L, "Senior Lecturer", "Chris Doe", "+79992223344", "@chris")
        ));

        TeacherResponseDto result = service.getTeacherById(3L);

        assertThat(result).isEqualTo(new TeacherResponseDto(3L, "Senior Lecturer", "Chris Doe",
                "+79992223344", "@chris"));
    }

    @Test
    void deleteTeacherRemovesTeacherAndReturnsDeletedSnapshot() {
        Teacher teacher = teacher(9L, "Professor", "Delete Me", "+79993334455", "@delete");
        when(repository.findById(9L)).thenReturn(Optional.of(teacher));

        TeacherResponseDto result = service.deleteTeacher(
                new TeacherRequestDto(9L, "Professor", "Delete Me", "+79993334455", "@delete")
        );

        assertThat(result).isEqualTo(new TeacherResponseDto(9L, "Professor", "Delete Me",
                "+79993334455", "@delete"));
        verify(repository).delete(teacher);
    }

    private Teacher teacher(long id, String degree, String fullName, String phoneNumber, String vkId) {
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setAcademicDegree(degree);
        teacher.setFullName(fullName);
        teacher.setPhoneNumber(phoneNumber);
        teacher.setVkId(vkId);
        return teacher;
    }

    private ArgumentMatcher<Teacher> matchesTeacher(Long id, String degree, String fullName,
                                                    String phoneNumber, String vkId) {
        return teacher -> teacher.getId() == (id == null ? 0L : id)
                && teacher.getAcademicDegree().equals(degree)
                && teacher.getFullName().equals(fullName)
                && java.util.Objects.equals(teacher.getPhoneNumber(), phoneNumber)
                && java.util.Objects.equals(teacher.getVkId(), vkId);
    }
}

package ru.andrewexe.schedule.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.andrewexe.schedule.entity.Teacher;

/**
 * DTO for {@link Teacher}
 */
public record TeacherResponseDto(Long Id, String academicDegree,
                                 @NotNull @NotEmpty String fullName,
                                 String phoneNumber, String vkId) {
}
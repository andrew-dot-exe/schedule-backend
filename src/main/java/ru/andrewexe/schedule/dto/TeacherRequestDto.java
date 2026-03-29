package ru.andrewexe.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

/**
 * DTO for {@link ru.andrewexe.schedule.entity.Teacher}
 */
public record TeacherRequestDto(Long Id, String academicDegree,
                                @NotEmpty(message = "Name cannot be empty!") @NotBlank(message = "Name cannot be blank!") String fullName,
                                String phoneNumber, String vkId) {
}
package ru.andrewexe.schedule.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * DTO for {@link ru.andrewexe.schedule.entity.User}
 */
public record UserRegisterDto(@NotNull @NotBlank @Length(min = 5) String login,
                              @NotNull @NotBlank @Length(min = 8) String password,
                              @NotNull @NotBlank String name) {
}

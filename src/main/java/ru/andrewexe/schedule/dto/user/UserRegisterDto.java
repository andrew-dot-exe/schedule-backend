package ru.andrewexe.schedule.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * DTO for {@link ru.andrewexe.schedule.entity.User}
 */
public record UserRegisterDto(@NotNull @NotEmpty @NotBlank @Length(min = 5) String login,
                              @NotNull @NotEmpty @NotBlank @Length(min = 8) byte[] password,
                              @NotNull @NotEmpty @NotBlank String name) {
}
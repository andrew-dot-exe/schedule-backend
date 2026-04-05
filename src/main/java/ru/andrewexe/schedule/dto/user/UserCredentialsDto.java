package ru.andrewexe.schedule.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserCredentialsDto(
        @NotNull @NotEmpty @NotBlank String login,
        @NotNull @NotEmpty @NotBlank byte[] password
)  {
}

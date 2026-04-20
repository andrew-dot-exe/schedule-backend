package ru.andrewexe.schedule.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserTokenDto(
        @NotNull @NotBlank String token
) {
}

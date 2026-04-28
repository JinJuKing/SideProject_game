package com.sideproject.game;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateGameRunRequest(
        @Size(max = 50)
        String guestName,

        @NotNull
        @DecimalMin("0.1")
        @DecimalMax("999999.99")
        Double survivalTimeSeconds,

        @NotNull
        @Min(1)
        @Max(999)
        Integer levelReached
) {
}

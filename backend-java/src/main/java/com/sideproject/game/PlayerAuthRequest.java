package com.sideproject.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlayerAuthRequest(
        @NotBlank
        @Size(min = 2, max = 50)
        String username,

        @NotBlank
        @Size(min = 4, max = 100)
        String password
) {
}

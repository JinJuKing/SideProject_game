package com.sideproject.game;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record GameRunResponse(
        Long id,
        String guestName,
        BigDecimal survivalTimeSeconds,
        Integer levelReached,
        OffsetDateTime createdAt
) {
    public static GameRunResponse from(GameRun gameRun) {
        return new GameRunResponse(
                gameRun.getId(),
                gameRun.getGuestName(),
                gameRun.getSurvivalTimeSeconds(),
                gameRun.getLevelReached(),
                gameRun.getCreatedAt()
        );
    }
}

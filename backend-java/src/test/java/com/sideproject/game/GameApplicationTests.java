package com.sideproject.game;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class GameApplicationTests {

    @Test
    void gameRunResponseFromEntity() {
        GameRun gameRun = new GameRun("guest", BigDecimal.valueOf(12.34), 3);

        GameRunResponse response = GameRunResponse.from(gameRun);

        assertThat(response.guestName()).isEqualTo("guest");
        assertThat(response.survivalTimeSeconds()).isEqualByComparingTo("12.34");
        assertThat(response.levelReached()).isEqualTo(3);
    }
}

package com.sideproject.game;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRunRepository extends JpaRepository<GameRun, Long> {

    List<GameRun> findTop10ByOrderBySurvivalTimeSecondsDescCreatedAtAsc();

    Optional<GameRun> findByPlayerId(Long playerId);
}

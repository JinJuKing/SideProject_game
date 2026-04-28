package com.sideproject.game;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameRunService {

    private final GameRunRepository gameRunRepository;

    public GameRunService(GameRunRepository gameRunRepository) {
        this.gameRunRepository = gameRunRepository;
    }

    @Transactional
    public GameRunResponse create(CreateGameRunRequest request) {
        String guestName = normalizeGuestName(request.guestName());
        BigDecimal survivalTime = BigDecimal.valueOf(request.survivalTimeSeconds())
                .setScale(2, RoundingMode.HALF_UP);

        GameRun saved = gameRunRepository.save(
                new GameRun(guestName, survivalTime, request.levelReached())
        );

        return GameRunResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<GameRunResponse> findTopRanking() {
        return gameRunRepository.findTop10ByOrderBySurvivalTimeSecondsDescCreatedAtAsc()
                .stream()
                .map(GameRunResponse::from)
                .toList();
    }

    private String normalizeGuestName(String guestName) {
        if (guestName == null || guestName.isBlank()) {
            return "guest";
        }
        return guestName.trim();
    }
}

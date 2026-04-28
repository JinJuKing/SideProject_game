package com.sideproject.game;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class GameRunService {

    private final GameRunRepository gameRunRepository;
    private final PlayerRepository playerRepository;

    public GameRunService(GameRunRepository gameRunRepository, PlayerRepository playerRepository) {
        this.gameRunRepository = gameRunRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public GameRunResponse create(CreateGameRunRequest request) {
        BigDecimal survivalTime = BigDecimal.valueOf(request.survivalTimeSeconds())
                .setScale(2, RoundingMode.HALF_UP);

        return createOrUpdatePlayerBest(request.playerId(), survivalTime, request.levelReached());
    }

    @Transactional(readOnly = true)
    public List<GameRunResponse> findTopRanking() {
        return gameRunRepository.findTop10ByOrderBySurvivalTimeSecondsDescCreatedAtAsc()
                .stream()
                .map(GameRunResponse::from)
                .toList();
    }

    private GameRunResponse createOrUpdatePlayerBest(Long playerId, BigDecimal survivalTime, Integer levelReached) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 플레이어입니다."));

        GameRun bestRun = gameRunRepository.findByPlayerId(playerId)
                .orElseGet(() -> gameRunRepository.save(new GameRun(player, survivalTime, levelReached)));

        if (survivalTime.compareTo(bestRun.getSurvivalTimeSeconds()) > 0) {
            bestRun.updateBestScore(survivalTime, levelReached);
        }

        return GameRunResponse.from(bestRun);
    }
}

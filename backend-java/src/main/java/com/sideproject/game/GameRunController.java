package com.sideproject.game;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game-runs")
@CrossOrigin(origins = "*")
public class GameRunController {

    private final GameRunService gameRunService;

    public GameRunController(GameRunService gameRunService) {
        this.gameRunService = gameRunService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameRunResponse create(@Valid @RequestBody CreateGameRunRequest request) {
        return gameRunService.create(request);
    }

    @GetMapping("/ranking")
    public List<GameRunResponse> ranking() {
        return gameRunService.findTopRanking();
    }
}

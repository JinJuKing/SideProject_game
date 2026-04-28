package com.sideproject.game;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "game_runs")
public class GameRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guest_name", nullable = false, length = 50)
    private String guestName;

    @Column(name = "survival_time_seconds", nullable = false, precision = 8, scale = 2)
    private BigDecimal survivalTimeSeconds;

    @Column(name = "level_reached", nullable = false)
    private Integer levelReached;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    protected GameRun() {
    }

    public GameRun(String guestName, BigDecimal survivalTimeSeconds, Integer levelReached) {
        this.guestName = guestName;
        this.survivalTimeSeconds = survivalTimeSeconds;
        this.levelReached = levelReached;
    }

    public Long getId() {
        return id;
    }

    public String getGuestName() {
        return guestName;
    }

    public BigDecimal getSurvivalTimeSeconds() {
        return survivalTimeSeconds;
    }

    public Integer getLevelReached() {
        return levelReached;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}

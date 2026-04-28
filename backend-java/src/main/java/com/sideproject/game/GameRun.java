package com.sideproject.game;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "game_runs")
public class GameRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Player player;

    @Column(name = "guest_name", length = 50)
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

    public GameRun(Player player, BigDecimal survivalTimeSeconds, Integer levelReached) {
        this.player = player;
        this.guestName = player.getUsername();
        this.survivalTimeSeconds = survivalTimeSeconds;
        this.levelReached = levelReached;
    }

    public Long getId() {
        return id;
    }

    public String getGuestName() {
        return guestName;
    }

    public Player getPlayer() {
        return player;
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

    public void updateBestScore(BigDecimal survivalTimeSeconds, Integer levelReached) {
        this.survivalTimeSeconds = survivalTimeSeconds;
        this.levelReached = levelReached;
        this.createdAt = OffsetDateTime.now();
    }
}

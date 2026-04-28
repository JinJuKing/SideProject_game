package com.sideproject.game;

public record PlayerResponse(
        Long id,
        String username
) {
    public static PlayerResponse from(Player player) {
        return new PlayerResponse(player.getId(), player.getUsername());
    }
}

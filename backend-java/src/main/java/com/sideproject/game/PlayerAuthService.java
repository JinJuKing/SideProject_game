package com.sideproject.game;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PlayerAuthService {

    private final PlayerRepository playerRepository;

    public PlayerAuthService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public PlayerResponse register(PlayerAuthRequest request) {
        String username = normalizeUsername(request.username());
        if (playerRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다.");
        }

        Player player = playerRepository.save(new Player(username, hashPassword(request.password())));
        return PlayerResponse.from(player);
    }

    @Transactional(readOnly = true)
    public PlayerResponse login(PlayerAuthRequest request) {
        String username = normalizeUsername(request.username());
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "닉네임 또는 비밀번호가 다릅니다."));

        if (!player.getPasswordHash().equals(hashPassword(request.password()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "닉네임 또는 비밀번호가 다릅니다.");
        }

        return PlayerResponse.from(player);
    }

    private String normalizeUsername(String username) {
        return username.trim();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : hash) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException error) {
            throw new IllegalStateException("SHA-256 algorithm is not available", error);
        }
    }
}

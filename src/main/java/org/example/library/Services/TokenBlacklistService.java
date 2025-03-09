package org.example.library.Services;

import org.example.library.Models.TokenBlacklist;
import org.example.library.Repositories.TokenBlacklistRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public TokenBlacklistService(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }
    public void blacklistToken(String token) {
        TokenBlacklist blacklistedToken = new TokenBlacklist(token);
        tokenBlacklistRepository.save(blacklistedToken);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.findByToken(token).isPresent();
    }
}
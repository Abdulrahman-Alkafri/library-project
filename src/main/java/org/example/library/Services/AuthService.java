package org.example.library.Services;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final TokenBlacklistService tokenBlacklistService;

    public AuthService(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public void logout(String token) {
        tokenBlacklistService.blacklistToken(token);
    }
}
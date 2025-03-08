package org.example.library.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.library.DTO.ApiResponse;
import org.example.library.DTO.PatronDTO;
import org.example.library.Models.Patron;
import org.example.library.Services.PatronService;
import org.example.library.Services.TokenBlacklistService;
import org.example.library.Utils.JWT.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private TokenBlacklistService tokenBlacklistService;
    private final PatronService patronService;
    private final JwtUtil jwtUtil;

    public AuthController(PatronService patronService, JwtUtil jwtUtil) {
        this.patronService = patronService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody PatronDTO patron) {
        try{
        Patron validPatron = patronService.authenticatePatron(patron.getEmail(), patron.getPassword());
        if (validPatron != null) {
            String token = jwtUtil.generateToken(validPatron.getEmail());
            return ResponseEntity.ok(new ApiResponse(true,"user logged in",token));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false,"Invalid credentials",null));
    } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,"error occurred",null));
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            tokenBlacklistService.blacklistToken(jwt);
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ApiResponse(true, "User logged out", null));
    }
}
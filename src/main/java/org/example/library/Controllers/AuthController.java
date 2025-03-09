package org.example.library.Controllers;

import jakarta.validation.Valid;
import org.example.library.DTO.ApiResponse;
import org.example.library.DTO.PatronDTO;
import org.example.library.Models.Patron;
import org.example.library.Services.AuthService;
import org.example.library.Services.PatronService;
import org.example.library.Services.TokenBlacklistService;
import org.example.library.Utils.JWT.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private TokenBlacklistService tokenBlacklistService;
    private final PatronService patronService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(PatronService patronService, AuthService authService, JwtUtil jwtUtil) {
        this.patronService = patronService;
        this.authService = authService;
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
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try{
            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            authService.logout(jwtToken);
            return ResponseEntity
                    .ok()
                    .body(new ApiResponse(
                            true,
                            "user logged out successfully",
                            null));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(
                            false,
                            "an error occured while logging out : " + e.getMessage(),
                            null));
        }
    }
}
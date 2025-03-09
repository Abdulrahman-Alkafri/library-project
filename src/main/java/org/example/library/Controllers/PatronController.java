package org.example.library.Controllers;

import jakarta.validation.Valid;
import org.example.library.DTO.ApiResponse;
import org.example.library.DTO.PatronDTOWithoutPassword;
import org.example.library.DTO.UpdatePatronDTO;
import org.example.library.Models.Patron;
import org.example.library.Services.AuthService;
import org.example.library.Services.PatronService;
import org.example.library.Utils.JWT.JwtUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {
    private final PatronService patronService;
    private final AuthService authService;

    public PatronController(PatronService patronService, AuthService authService) {
        this.patronService = patronService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPatrons() {
        List<PatronDTOWithoutPassword> patrons = patronService.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "Patrons retrieved successfully", patrons));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPatron(@PathVariable Long id) {
        return patronService.findById(id)
                .map(patron -> ResponseEntity.ok(new ApiResponse(true, "Patron retrieved successfully", patron)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Patron not found", null)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createPatron(@Valid @RequestBody Patron patron) {
        Patron savedPatron = patronService.save(patron);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Patron created successfully", savedPatron));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePatron(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePatronDTO updatedPatron,
            @RequestHeader("Authorization") String token) {
        try {
            // Step 1: Log out the user (invalidate the current token)
            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            authService.logout(jwtToken);

            // Step 2: Update the patron's data
            Optional<PatronDTOWithoutPassword> updatedPatronOptional = patronService.updatePatron(id, updatedPatron);
            if (updatedPatronOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Patron not found", null));
            }

            PatronDTOWithoutPassword updatedPatronEntity = updatedPatronOptional.get();

            // Step 3: Log in the user again (generate a new token)
            String newToken = JwtUtil.generateToken(updatedPatronEntity.getEmail());

            return ResponseEntity.ok(new ApiResponse(true, "Patron updated successfully", newToken));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An error occurred: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePatron(@PathVariable Long id) {
        if (patronService.findById(id).isPresent()) {
            patronService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse(true, "Patron deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Patron not found", null));
        }
    }
}
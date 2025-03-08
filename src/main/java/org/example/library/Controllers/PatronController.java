package org.example.library.Controllers;

import jakarta.validation.Valid;
import org.example.library.DTO.ApiResponse;
import org.example.library.Models.Patron;
import org.example.library.Services.PatronService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
@Validated
public class PatronController {
    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping
    @Cacheable(value = "patronCache")
    public ResponseEntity<ApiResponse> getAllPatrons() {
        List<Patron> patrons = patronService.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "Patrons retrieved successfully", patrons));
    }

    @GetMapping("/{id}")
    @Cacheable(value = "patronCache",key = "#id")
    public ResponseEntity<ApiResponse> getPatron(@PathVariable Long id) {
        return patronService.findById(id)
                .map(patron -> ResponseEntity.ok(new ApiResponse(true, "Patron retrieved successfully", patron)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Patron not found", null)));
    }

    @PostMapping
    @CacheEvict(value = "patronCache",allEntries = true)
    public ResponseEntity<ApiResponse> createPatron(@Valid @RequestBody Patron patron) {
        Patron savedPatron = patronService.save(patron);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Patron created successfully", savedPatron));
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "patronCache",key = "#id")
    public ResponseEntity<ApiResponse> updatePatron(@PathVariable Long id, @Valid @RequestBody Patron patron) {
        patron.setId(id);
        return patronService.findById(id)
                .map(existingPatron -> {
                    Patron updatedPatron = patronService.save(patron);
                    return ResponseEntity.ok(new ApiResponse(true, "Patron updated successfully", updatedPatron));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Patron not found", null)));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "patronCache",key = "#id")
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
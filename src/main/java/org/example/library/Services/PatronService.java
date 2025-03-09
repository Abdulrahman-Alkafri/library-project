package org.example.library.Services;

import org.example.library.DTO.UpdatePatronDTO;
import org.example.library.Models.Patron;
import org.example.library.Repositories.PatronRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatronService {
    private final PatronRepository patronRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CaffeineCacheManager cacheManager;


    public PatronService(PatronRepository patronRepository,@Qualifier("patronsCacheManager") CaffeineCacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.patronRepository = patronRepository;
    }

    public Patron authenticatePatron(String email, String password) {
        Patron patron = patronRepository.findByEmail(email);
        if (patron != null && passwordEncoder.matches(password, patron.getPassword())) {
            return patron;
        }
        return null; // Invalid credentials
    }

    @Cacheable(value = "patrons")
    public List<Patron> findAll() {
        return patronRepository.findAll();
    }

    @Cacheable(value = "patrons", key = "#id")
    public Optional<Patron> findById(Long id) {
        return patronRepository.findById(id);
    }

    @CacheEvict(value = "books", allEntries = true)
    public Patron save(Patron patron) {
        patron.setPassword(passwordEncoder.encode(patron.getPassword()));
        return patronRepository.save(patron);
    }

    @CacheEvict(value = "books", allEntries = true)
    public Optional<Patron> updatePatron(Long id, UpdatePatronDTO updatedPatron) {
        return patronRepository.findById(id)
                .map(existingPatron -> {
                    // Update fields if they are not null in the updatedPatron
                    if (updatedPatron.getName() != null) {
                        existingPatron.setName(updatedPatron.getName());
                    }
                    if (updatedPatron.getEmail() != null) {
                        existingPatron.setEmail(updatedPatron.getEmail());
                    }
                    return patronRepository.save(existingPatron);
                });
    }

    @CacheEvict(value = "patrons", key = "#id")
    public void deleteById(Long id) {
        patronRepository.deleteById(id);
    }
}
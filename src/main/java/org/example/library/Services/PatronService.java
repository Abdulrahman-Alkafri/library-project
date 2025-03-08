package org.example.library.Services;

import org.example.library.Models.Patron;
import org.example.library.Repositories.PatronRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatronService {
    private final PatronRepository patronRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    public Patron authenticatePatron(String email, String password) {
        Patron patron = patronRepository.findByEmail(email);
        if (patron != null && passwordEncoder.matches(password, patron.getPassword())) {
            return patron;
        }
        return null; // Invalid credentials
    }

    public List<Patron> findAll() {
        return patronRepository.findAll();
    }

    public Optional<Patron> findById(Long id) {
        return patronRepository.findById(id);
    }

    public Patron save(Patron patron) {
        return patronRepository.save(patron);
    }

    public void deleteById(Long id) {
        patronRepository.deleteById(id);
    }
}
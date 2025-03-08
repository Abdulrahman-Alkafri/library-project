package org.example.library.Services;

import org.example.library.Models.Patron;
import org.example.library.Repositories.PatronRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PatronRepository patronRepository;

    public CustomUserDetailsService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Patron patron = patronRepository.findByEmail(username); // Assuming email is used for authentication
        if (patron == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                patron.getEmail(),
                patron.getPassword(),
                new ArrayList<>()
        );
    }
}
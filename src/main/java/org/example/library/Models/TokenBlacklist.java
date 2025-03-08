package org.example.library.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TokenBlacklist")
@Table(name = "token_blacklists")
public class TokenBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "token",nullable = false)
    @Size(min = 50, max = 500, message = "Token length must be between 50 and 500 characters")
    private String token;

    @Column(name = "created_at",nullable = false)
    private Date createdAt;

    public TokenBlacklist(String token) {
        this.token = token;
        this.createdAt = new Date();
    }
}
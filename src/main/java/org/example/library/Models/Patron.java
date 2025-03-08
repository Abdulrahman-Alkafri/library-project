package org.example.library.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity(name = "Patron")
@Table(name = "patrons")
public class Patron {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "name",nullable = false,unique = true)
    @NotBlank
    private String name;


    @Column(name = "email",nullable = false,unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(name = "password")
    @NotBlank
    @Size(min = 6,max = 12,message = "password length must be in the range of (6 -> 12)")
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "patron",cascade = CascadeType.ALL)
    private List<BorrowingRecord> borrowingRecords;
}
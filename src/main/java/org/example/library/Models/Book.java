package org.example.library.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Book")
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 0, max = 255, message = "title size must not exceed 255")
    private String title;

    @Size(min = 3, max = 50, message = "author size must not exceed 50")
    private String author;

    @NotNull
    private int publicationYear;

    @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters long")
    private String isbn;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonManagedReference("book-borrowing") // Explicit name to avoid conflicts
    private List<BorrowingRecord> borrowingRecords;

    @Column(name = "is_available")
    private boolean available = true;  // Default to true
}
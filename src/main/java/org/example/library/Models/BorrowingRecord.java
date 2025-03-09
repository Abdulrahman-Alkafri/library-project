package org.example.library.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.library.Utils.ValidBorrowingDates;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "borrowing_records")
@ValidBorrowingDates // Custom validation
public class BorrowingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference("book-borrowing") // Explicit name to match with Book
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @JsonBackReference("patron-borrowing") // Explicit name to match with Patron
    @ManyToOne
    @JoinColumn(name = "patron_id", nullable = false)
    private Patron patron;

    @NotNull
    private LocalDate borrowDate;

    private LocalDate returnDate;
}
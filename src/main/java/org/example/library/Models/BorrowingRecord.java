package org.example.library.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.example.library.Utils.ValidBorrowingDates;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "borrowing_records")
@ValidBorrowingDates // custom validation to ensure borrowing date is before the return date
public class BorrowingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "patron_id", nullable = false)
    private Patron patron;

    @NotNull
    private LocalDate borrowDate;

    @NotNull
    private LocalDate returnDate;
}
package org.example.library.Services;

import org.example.library.DTO.ApiResponse;
import org.example.library.Models.Book;
import org.example.library.Models.BorrowingRecord;
import org.example.library.Models.Patron;
import org.example.library.Repositories.BookRepository;
import org.example.library.Repositories.BorrowingRecordRepository;
import org.example.library.Repositories.PatronRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BorrowingRecordService{
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    public BorrowingRecordService(BorrowingRecordRepository borrowingRecordRepository, BookRepository bookRepository, PatronRepository patronRepository) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }

    @Transactional
    public ApiResponse borrowBook(Long bookId, Long patronId) {
        // Validate that the book exists
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            return new ApiResponse(false, "Book not found", null);
        }

        // Validate that the patron exists
        Optional<Patron> patronOpt = patronRepository.findById(patronId);
        if (patronOpt.isEmpty()) {
            return new ApiResponse(false, "Patron not found", null);
        }

        // Check if the book is already borrowed (optional, similar logic can be added)
        // Logic to check if there's an existing borrowing record for this book and patron can be added

        // Create a new borrowing record
        BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                .book(bookOpt.get())
                .patron(patronOpt.get())
                .borrowDate(LocalDate.now())
                .build();

        borrowingRecordRepository.save(borrowingRecord);
        return new ApiResponse(true, "Book borrowed successfully", borrowingRecord);
    }

    @Transactional
    public ApiResponse returnBook(Long bookId, Long patronId) {
        // Find borrowing record by bookId and patronId
        Optional<BorrowingRecord> recordOpt = borrowingRecordRepository.findByBookAndPatron(bookId, patronId);
        if (recordOpt.isEmpty()) {
            return new ApiResponse(false, "No borrowing record found for this book and patron", null);
        }

        BorrowingRecord borrowingRecord = recordOpt.get();
        borrowingRecord.setReturnDate(LocalDate.now());  // Set return date
        borrowingRecordRepository.save(borrowingRecord);

        return new ApiResponse(true, "Book returned successfully", borrowingRecord);
    }
}
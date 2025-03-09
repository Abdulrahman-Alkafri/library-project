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
public class BorrowingRecordService {

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
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            return new ApiResponse(false, "Book not found", null);
        }

        Optional<Patron> patronOpt = patronRepository.findById(patronId);
        if (patronOpt.isEmpty()) {
            return new ApiResponse(false, "Patron not found", null);
        }

        Optional<BorrowingRecord> existingRecordOpt = borrowingRecordRepository.findByBookAndPatron(bookId, patronId);
        if (existingRecordOpt.isPresent()) {
            return new ApiResponse(false, "Book is already borrowed by this patron", null);
        }

        Book book = bookOpt.get();
        if (!book.isAvailable()) {
            return new ApiResponse(false, "Book is currently unavailable", null);
        }

        BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                .book(book)
                .patron(patronOpt.get())
                .borrowDate(LocalDate.now())
                .returnDate(null)
                .build();

        borrowingRecordRepository.save(borrowingRecord);

        book.setAvailable(false);
        bookRepository.save(book);

        return new ApiResponse(true, "Book borrowed successfully", borrowingRecord);
    }


    @Transactional
    public ApiResponse returnBook(Long bookId, Long patronId) {
        Optional<BorrowingRecord> recordOpt = borrowingRecordRepository.findByBookAndPatron(bookId, patronId);

        if (recordOpt.isEmpty()) {
            return new ApiResponse(false, "No active borrowing record found for this book and patron", null);
        }

        BorrowingRecord borrowingRecord = recordOpt.get();

        if (borrowingRecord.getReturnDate() != null) {
            return new ApiResponse(false, "This book has already been returned", null);
        }


        borrowingRecord.setReturnDate(LocalDate.now());
        borrowingRecordRepository.save(borrowingRecord);

        Book book = borrowingRecord.getBook();
        book.setAvailable(true);
        bookRepository.save(book);
        return new ApiResponse(true, "Book returned successfully", borrowingRecord);
    }
}
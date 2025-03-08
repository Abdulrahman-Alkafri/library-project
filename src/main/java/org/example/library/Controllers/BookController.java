package org.example.library.Controllers;

import jakarta.validation.Valid;
import org.example.library.DTO.ApiResponse;
import org.example.library.Models.Book;
import org.example.library.Services.BookService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Cacheable("booksCache") // Enable caching for getAllBooks
    public ResponseEntity<ApiResponse> getAllBooks() {
        List<Book> books = bookService.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "Books retrieved successfully", books));
    }

    @GetMapping("/{id}")
    @Cacheable(value = "booksCache", key = "#id") // Enable caching for getBook, using 'id' as the key
    public ResponseEntity<ApiResponse> getBook(@PathVariable Long id) {
        return bookService.findById(id)
                .map(book -> ResponseEntity.ok(new ApiResponse(true, "Book retrieved successfully", book)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Book not found", null)));
    }

    @PostMapping
    @CacheEvict(value = "booksCache", allEntries = true) // Clear the entire cache when creating a book
    public ResponseEntity<ApiResponse> createBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.save(book);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Book created successfully", savedBook));
    }


    @PutMapping("/{id}")
    @CacheEvict(value = "booksCache", key = "#id") // Evict the cache for the specific book
    public ResponseEntity<ApiResponse> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        book.setId(id);
        return bookService.findById(id)
                .map(existingBook -> {
                    Book updatedBook = bookService.save(book);
                    return ResponseEntity.ok(new ApiResponse(true, "Book updated successfully", updatedBook));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Book not found", null)));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "booksCache", key = "#id") // Evict the cache for the specific book
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long id) {
        if (bookService.findById(id).isPresent()) {
            bookService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse(true, "Book deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Book not found", null));
        }
    }
}
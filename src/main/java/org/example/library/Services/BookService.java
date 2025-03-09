package org.example.library.Services;

import org.example.library.Models.Book;
import org.example.library.Repositories.BookRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final CaffeineCacheManager cacheManager;

    public BookService(@Qualifier("booksCacheManager") CaffeineCacheManager cacheManager, BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = "books")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Cacheable(value = "books", key = "#id")
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @CacheEvict(value = "books", allEntries = true)
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @CacheEvict(value = "books", allEntries = true)
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @CacheEvict(value = "books", allEntries = true)
    public Optional<Book> updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    // Update fields
                    existingBook.setTitle(updatedBook.getTitle());
                    existingBook.setAuthor(updatedBook.getAuthor());
                    existingBook.setPublicationYear(updatedBook.getPublicationYear());
                    existingBook.setIsbn(updatedBook.getIsbn());
                    existingBook.setAvailable(updatedBook.isAvailable());

                    // Save and return the updated book
                    return bookRepository.save(existingBook);
                });
    }
}
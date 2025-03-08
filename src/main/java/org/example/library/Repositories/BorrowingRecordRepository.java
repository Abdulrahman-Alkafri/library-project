package org.example.library.Repositories;

import org.example.library.Models.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    @Query(value = "SELECT * FROM borrowing_records AS br WHERE br.book_id = :bookId AND br.patron_id = :patronId; ",nativeQuery = true)
    Optional<BorrowingRecord> findByBookAndPatron(Long bookId, Long patronId);
}
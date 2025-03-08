package org.example.library.Controllers;

import org.example.library.DTO.ApiResponse;
import org.example.library.Services.BorrowingRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
public class BorrowingController {

    private final BorrowingRecordService borrowingRecordService;

    public BorrowingController(BorrowingRecordService borrowingRecordService) {
        this.borrowingRecordService = borrowingRecordService;
    }

    @PostMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity<ApiResponse> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        try {
            ApiResponse response = borrowingRecordService.borrowBook(bookId, patronId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An error occurred: " + e.getMessage(), null));
        }
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<ApiResponse> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        try {
            ApiResponse response = borrowingRecordService.returnBook(bookId, patronId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An error occurred: " + e.getMessage(), null));
        }
    }
}
package org.example.library.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.library.Models.BorrowingRecord;

public class BorrowingDatesValidator implements ConstraintValidator<ValidBorrowingDates, BorrowingRecord> {

    @Override
    public boolean isValid(BorrowingRecord record, ConstraintValidatorContext context) {
        if (record.getBorrowDate() == null || record.getReturnDate() == null) {
            return true; // Not validating if dates are null (handled by @NotNull)
        }
        return !record.getReturnDate().isBefore(record.getBorrowDate());
    }
}
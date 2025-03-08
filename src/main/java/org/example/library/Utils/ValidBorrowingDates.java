package org.example.library.Utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BorrowingDatesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBorrowingDates {
    String message() default "Return date must be after borrow date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
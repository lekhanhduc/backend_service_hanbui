package com.javabuilder.backendservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateOfBirthValidator implements ConstraintValidator<DateOfBirth, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if(value == null) return false;

        LocalDate now = LocalDate.now();
        return value.plusYears(16).isBefore(now) || value.plusYears(16).isEqual(now);
    }
}

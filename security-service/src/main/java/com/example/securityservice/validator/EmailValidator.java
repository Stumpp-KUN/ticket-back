package com.example.securityservice.validator;

import com.example.securityservice.exception.EmailValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_LENGTH_REGEX = "^.{1,100}$";
    private static final String EMAIL_DOG_REGEX = ".*@.*";
    private static final String EMAIL_DOT_REGEX = ".*\\..*";
    private static final String EMAIL_LEADING_TRAILING_DOG_REGEX = "^(?!@|.*@\\.|.*\\.@)[^@].*[^@]$";
    private static final String EMAIL_LEADING_TRAILING_DOT_REGEX = "^(?!\\.|.*\\..$).*";

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }
    // TODO: переписать if, switch
    @SneakyThrows
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            throw new EmailValidationException("Email is null");
        }

        if (email == "") {
            throw new EmailValidationException("Please fill out the required field");
        }

        if (!Pattern.matches(EMAIL_LENGTH_REGEX, email)) {
            throw new EmailValidationException("Email length must be between 1 and 100 characters");
        }

        if (!Pattern.matches(EMAIL_DOG_REGEX, email)) {
            throw new EmailValidationException("Email must contain at least one @");
        }

        if (!Pattern.matches(EMAIL_DOT_REGEX, email)) {
            throw new EmailValidationException("Email must contain at least one dot");
        }

        if (!Pattern.matches(EMAIL_LEADING_TRAILING_DOG_REGEX, email)) {
            throw new EmailValidationException("Leading or trailing @ is not allowed");
        }

        if (!Pattern.matches(EMAIL_LEADING_TRAILING_DOT_REGEX, email)) {
            throw new EmailValidationException("Leading or trailing dot is not allowed");
        }

        return true;
    }
}

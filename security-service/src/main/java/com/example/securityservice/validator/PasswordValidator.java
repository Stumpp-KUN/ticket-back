package com.example.securityservice.validator;

import com.example.securityservice.exception.PasswordValidationException;
import com.example.securityservice.validator.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String LOWERCASE_REGEX = ".*[a-z].*";
    private static final String UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String DIGIT_REGEX = ".*\\d.*";
    private static final String SPECIAL_CHAR_REGEX = ".*[~.\"(),:;<>@\\[\\]!#$%&'*+-/=?^_`{|}].*";
    private static final String PASSWORD_LENGTH_REGEX = ".{6,20}";

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @SneakyThrows
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null) {
            throw new PasswordValidationException("Password is null");
        }

        if (password == "") {
            throw new PasswordValidationException("Please fill out the required field");
        }

        if (!Pattern.matches(PASSWORD_LENGTH_REGEX, password)) {
            throw new PasswordValidationException("Password length must be between 6 and 20 characters");
        }

        if (!Pattern.matches(LOWERCASE_REGEX, password)) {
            throw new PasswordValidationException("Password must contain at least one lowercase letter");
        }

        if (!Pattern.matches(UPPERCASE_REGEX, password)) {
            throw new PasswordValidationException("Password must contain at least one uppercase letter");
        }

        if (!Pattern.matches(DIGIT_REGEX, password)) {
            throw new PasswordValidationException("Password must contain at least one digit");
        }

        if (!Pattern.matches(SPECIAL_CHAR_REGEX, password)) {
            throw new PasswordValidationException("Password must contain at least one special character");
        }

        return true;
    }
}

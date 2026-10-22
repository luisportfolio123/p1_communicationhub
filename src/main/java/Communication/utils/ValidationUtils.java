package com.example.demo.util;

import org.apache.commons.validator.routines.EmailValidator;

public final class ValidationUtils {

    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_VALIDATOR.isValid(email.trim());//true
    }
}
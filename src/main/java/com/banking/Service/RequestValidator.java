package com.banking.Service;

import java.util.regex.Pattern;

public class RequestValidator {

    public boolean isValidPassword(String password) {
        // Minimum 8 characters, at least one uppercase letter, one lowercase letter, one number and one special character
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.compile(passwordPattern).matcher(password).matches();
    }
    public boolean isValidEmail(String email) {
        // Basic email pattern
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }
    public boolean isValidPhoneNumber(String phoneNumber) {
        // Basic phone number validation (10 digits)
        String phonePattern = "^\\d{10}$";
        return Pattern.compile(phonePattern).matcher(phoneNumber).matches();
    }
}

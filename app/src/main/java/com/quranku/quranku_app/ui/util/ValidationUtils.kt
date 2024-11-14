package com.quranku.quranku_app.ui.util

import java.util.regex.Pattern

// Validasi email
fun validateEmail(email: String): String? {
    val emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"
    return when {
        email.isBlank() -> "Email is required"
        !Pattern.matches(emailRegex, email) -> "Email format is invalid"
        email.length < 5 || email.length > 90 -> "Email must be between 5 and 90 characters"
        else -> null
    }
}

// Validasi password
fun validatePassword(password: String): String? {
    val passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,90}$" // Huruf besar, angka, panjang 8-90 karakter
    return when {
        password.isBlank() -> "Password is required"
        !Pattern.matches(passwordRegex, password) -> "Password must contain at least one uppercase letter, one number, and be 8-90 characters long"
        else -> null
    }
}
package com.quranku.quranku_app.data.models

data class RegisterResponse(
    val msg: String
)

data class RegisterRequest(
    val full_name: String,
    val email: String,
    val password: String
)

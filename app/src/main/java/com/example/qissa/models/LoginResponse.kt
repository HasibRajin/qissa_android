package com.example.qissa.models

data class LoginResponse(
    val `data`: UserData,
    val message: String,
    val success: Boolean
)
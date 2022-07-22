package com.example.qissa.models

data class SignupResponse(
    val `data`: UserData,
    val message: String,
    val success: Boolean
)

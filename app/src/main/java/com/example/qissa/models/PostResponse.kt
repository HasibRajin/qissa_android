package com.example.qissa.models

data class PostResponse(
    val `data`: PostData,
    val message: String,
    val success: Boolean
)
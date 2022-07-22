package com.example.qissa.models

data class Reaction(
    val created_at: String,
    var id: Int,
    val post_id: Int,
    val reaction_type: String,
    val updated_at: String,
    val user_id: Int
)
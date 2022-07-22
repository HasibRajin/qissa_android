package com.example.qissa.models

data class DataX(
    val created_at: String,
    val details: String,
    val id: Int,
    val image: Any,
    val meta: Meta,
    var reactions: List<Reaction>,
    val title: String,
    val topic_id: Int,
    val updated_at: String,
    val user: UserX,
    val user_id: Int
)

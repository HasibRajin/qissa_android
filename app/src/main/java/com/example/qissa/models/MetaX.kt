package com.example.qissa.models

data class MetaX(
    val current_page: Int,
    val first_page: Int,
    val first_page_url: String,
    val last_page: Int,
    val last_page_url: String,
    val next_page_url: String,
    val per_page: Int,
    val previous_page_url: Any,
    val total: Int
)

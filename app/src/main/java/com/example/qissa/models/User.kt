package com.example.qissa.models

data class User(
    val created_at: String,
    val email: String,
    val id: Int,
    val is_active: Boolean,
    val name: String,
    val profile_pic: String?,
    val updated_at: String

)
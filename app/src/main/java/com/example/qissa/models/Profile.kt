package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("address")
    val address: Any?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("date_of_birth")
    val dateOfBirth: Any?,
    @SerializedName("education")
    val education: Any?,
    @SerializedName("gender")
    val gender: Any?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("phone")
    val phone: Any?,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int
)

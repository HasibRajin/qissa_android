package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class DataXXXXXX(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("date_of_birth")
    val dateOfBirth: String?,
    @SerializedName("education")
    val education: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("user_id")
    val userId: Int?,
    @SerializedName("profile_pic")
    val profilePic: String?
)

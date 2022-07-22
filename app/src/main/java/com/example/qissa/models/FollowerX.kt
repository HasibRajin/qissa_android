package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class FollowerX(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_active")
    val isActive: Any?,
    @SerializedName("meta")
    val meta: MetaXXXX,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_pic")
    val profilePic: Any?,
    @SerializedName("updated_at")
    val updatedAt: String
)

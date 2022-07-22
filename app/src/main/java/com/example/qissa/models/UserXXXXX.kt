package com.example.qissa.models


import com.google.gson.annotations.SerializedName

data class UserXXXXX(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("meta")
    val meta: MetaXXXXXXXXX?,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_pic")
    val profilePic: String?,
    @SerializedName("updated_at")
    val updatedAt: String
)
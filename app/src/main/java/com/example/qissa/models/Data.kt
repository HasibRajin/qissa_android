package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("follower")
    val follower: List<Follower>?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("meta")
    val meta: MetaXXX,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile")
    val profile: Profile,
    @SerializedName("profile_pic")
    val profilePic: Any?,
    @SerializedName("updated_at")
    val updatedAt: String
)

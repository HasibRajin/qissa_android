package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class DataXXXXXXXXXXXXX(
    @SerializedName("post")
    val post: List<Post>,
    @SerializedName("topic")
    val topic: List<Topic>,
    @SerializedName("user")
    val user: List<UserXXXXX>
)

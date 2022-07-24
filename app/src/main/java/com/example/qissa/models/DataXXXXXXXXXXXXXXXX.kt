package com.example.qissa.models


import com.google.gson.annotations.SerializedName

data class DataXXXXXXXXXXXXXXXX(
    @SerializedName("answer_details")
    val answerDetails: String,
    @SerializedName("claps")
    val claps: List<Clap>,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("meta")
    val meta: MetaXXXXXXXXXXXXX,
    @SerializedName("question_id")
    val questionId: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user")
    val user: UserXXXXXX,
    @SerializedName("user_id")
    val userId: Int
)
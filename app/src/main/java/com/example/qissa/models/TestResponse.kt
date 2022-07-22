package com.example.qissa.models

import java.io.Serializable

data class TestResponse(
    val test: String?,
    val success: Boolean
): Serializable
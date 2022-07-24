package com.example.qissa.repositories

import android.content.Context
import com.example.qissa.models.QuestionResponse
import com.example.qissa.network.ApiInterface
import com.example.qissa.network.SafeApiRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuestionRepositories @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val api: ApiInterface
) {
    suspend fun callGetQuestionApi(currentPage: Int, likerId: Int?): QuestionResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.getQuestion(currentPage, likerId)
            }
        }
    }
}

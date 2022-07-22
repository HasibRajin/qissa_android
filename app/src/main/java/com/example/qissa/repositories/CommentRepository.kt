package com.example.qissa.repositories

import android.content.Context
import com.example.qissa.models.CommentResponse
import com.example.qissa.models.CreateCommentResponse
import com.example.qissa.network.ApiInterface
import com.example.qissa.network.SafeApiRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CommentRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ApiInterface
) {
    suspend fun callGetCommentApi(postId: Int, token: String): CommentResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.getComment(postId, token)
            }
        }
    }

    suspend fun callCreateCommentApi(
        token: String,
        commentText: String,
        postId: Int
    ): CreateCommentResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.doComment(token, commentText, postId)
            }
        }
    }
}

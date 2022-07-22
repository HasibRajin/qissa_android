package com.example.qissa.repositories

import android.content.Context
import com.example.qissa.models.ReactionResponse
import com.example.qissa.network.ApiInterface
import com.example.qissa.network.SafeApiRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReactionRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ApiInterface
) {
    suspend fun callCreateReactionApi(
        token: String,
        postId: Int,
        reactionType: String
    ): ReactionResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.doLike(token, postId, reactionType)
            }
        }
    }

    suspend fun callDeleteReactionApi(reactionId: Int, token: String): ReactionResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.deleteLike(reactionId, token)
            }
        }
    }
}

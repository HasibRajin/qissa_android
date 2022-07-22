package com.example.qissa.repositories

import android.content.Context
import com.example.qissa.models.FollowerResponse
import com.example.qissa.models.FollowingResponse
import com.example.qissa.models.UserRelationRespose
import com.example.qissa.network.ApiInterface
import com.example.qissa.network.SafeApiRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRelationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ApiInterface
) {
    suspend fun callCreateRelationApi(
        token: String,
        userId: Int,
        relationType: String
    ): UserRelationRespose {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.doFollow(token, userId, relationType)
            }
        }
    }

    suspend fun callDeleteRelationApi(userId: Int, token: String): UserRelationRespose {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.deleteRelation(userId, token)
            }
        }
    }

    suspend fun callGetRelationApi(
        token: String,
        currentPage: Int,
        relatableId: Int,
        relatableType: String
    ): FollowerResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.getRelation(token, currentPage, relatableId, relatableType)
            }
        }
    }

    suspend fun callGetFollowing(
        token: String,
        currentPage: Int,
        relatableId: Int,
        relatableType: String
    ): FollowingResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.getFollowing(token, currentPage, relatableId, relatableType)
            }
        }
    }
}

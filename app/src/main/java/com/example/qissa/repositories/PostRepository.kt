package com.example.qissa.repositories

import android.content.Context
import com.example.qissa.models.CreatePostResponse
import com.example.qissa.models.PostResponse
import com.example.qissa.network.ApiInterface
import com.example.qissa.network.SafeApiRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ApiInterface
) {
    suspend fun callGetPostApi(currentPage: Int, likerId: Int?): PostResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.getPost(currentPage, likerId)
            }
        }
    }

    suspend fun getPostsPaged(currentPage: Int): PostResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.getPagedPost(currentPage)
            }
        }
    }

    suspend fun getSingleUserPosts(userId: Int, currentPage: Int, likerId: Int?): PostResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.getSingleUserPost(userId, currentPage, likerId)
            }
        }
    }

    suspend fun getTopicUserPosts(
        topicId: Int,
        currentPage: Int,
        userId: Int?
    ): PostResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.getTopicUserPost(topicId, currentPage, userId, userId)
            }
        }
    }

    suspend fun callDoPostApi(
        token: String,
        title: String?,
        details: String,
        image: String?,
        topicId: Int
    ): CreatePostResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.doPost(token, title, details, image, topicId)
            }
        }
    }

    suspend fun callDeletePostApi(
        token: String,
        postId: Int
    ): CreatePostResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.deletePost(postId, token)
            }
        }
    }
}

package com.example.qissa.repositories

import android.content.Context
import com.example.qissa.models.SingleUserResponse
import com.example.qissa.network.ApiInterface
import com.example.qissa.network.SafeApiRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ApiInterface
) {
    suspend fun callGetUserApi(userID: Int, followerId: Int?): SingleUserResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.getUserData(userID, followerId)
            }
        }
    }
}

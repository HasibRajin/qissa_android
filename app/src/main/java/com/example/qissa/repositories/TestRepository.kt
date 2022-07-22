package com.example.qissa.repositories

import android.content.Context
import com.example.qissa.models.TestResponse
import com.example.qissa.network.ApiInterface
import com.example.qissa.network.SafeApiRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TestRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ApiInterface
) {
    suspend fun getTest(): TestResponse {
        return withContext(Dispatchers.IO) {

            SafeApiRequest.apiRequest(context) {
                api.getTestData()
            }
        }
    }
}

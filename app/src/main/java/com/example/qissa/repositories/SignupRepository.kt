package com.example.qissa.repositories

import android.content.Context
import com.example.qissa.models.SignupResponse
import com.example.qissa.network.ApiInterface
import com.example.qissa.network.SafeApiRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignupRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ApiInterface
) {
    suspend fun callSignupApi(name: String, email: String, password: String, confirm_password: String): SignupResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.doSignup(name, email, password, confirm_password)
            }
        }
    }
}
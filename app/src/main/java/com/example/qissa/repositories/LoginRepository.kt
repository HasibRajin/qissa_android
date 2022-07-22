package com.example.qissa.repositories

import android.content.Context
import android.util.Log
import com.example.qissa.dialogue.LoginBottomDialogue
import com.example.qissa.models.LoginResponse
import com.example.qissa.network.ApiInterface
import com.example.qissa.network.SafeApiRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ApiInterface
) {
    suspend fun callLoginApi(email: String, password: String): LoginResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                Log.d(
                    LoginBottomDialogue.TAG,
                    "doSignInWDFEWRHTRYJTUKYILUYKUTJYWDDEFWGRTRHYJUKILKUYJEFWDW: Signing clicked"
                )

                api.doLogin(email, password)
            }
        }
    }

    suspend fun callSocialLoginApi(name: String, email: String, profilePic: String): LoginResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {

                api.doSocialLogin(name, email, profilePic)
            }
        }
    }
}

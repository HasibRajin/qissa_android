package com.example.qissa.repositories

import android.content.Context
import com.example.qissa.models.UpdateProfileResponse
import com.example.qissa.network.ApiInterface
import com.example.qissa.network.SafeApiRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ApiInterface
) {
    suspend fun callUpdateUserInfoApi(
        token: String,
        name: String,
        email: String,
        phone: String,

    ): UpdateProfileResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.doUpdateUserInfo(
                    token,
                    name,
                    email,
                    phone,
                )
            }
        }
    }

    suspend fun callUpdateProfileInfoApi(
        token: String,
        dateOfBirth: String,
        gender: String,
        education: String,
        address: String
    ): UpdateProfileResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.doUpdateProfileInfo(
                    token,
                    dateOfBirth,
                    gender,
                    education,
                    address
                )
            }
        }
    }

    suspend fun callUpdateProfileImageApi(
        token: String,
        profilePic: String,

    ): UpdateProfileResponse {
        return withContext(Dispatchers.IO) {
            SafeApiRequest.apiRequest(context) {
                api.doUpdateProfileImage(
                    token,
                    profilePic,
                )
            }
        }
    }
}

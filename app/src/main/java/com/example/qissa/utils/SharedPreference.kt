package com.example.qissa.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.databinding.library.BuildConfig
import com.example.qissa.models.Data
import com.example.qissa.models.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject

class SharedPreference @Inject constructor(
    context: Context
) {
    companion object {
        private const val PREF_TOKEN = "Token"
        private const val PREF_USER = "User"
        private const val PROFILE = "PROFILE"
        private const val PATH_IMAGE = "PathImg"
        private const val LOGIN = "Login"
    }

    private val context = context.applicationContext

    @Volatile
    private var userSharePreference: SharedPreferences? = null

    @Volatile
    private var user: User? = null

    //    @Volatile
    private var profile: Data? = null

    private fun getUserSharePreference(): SharedPreferences {
        return userSharePreference ?: synchronized(this) {
            context.getSharedPreferences(
                "${BuildConfig.LIBRARY_PACKAGE_NAME}.main",
                Context.MODE_PRIVATE
            )
        }
    }

    fun reset() {
        getUserSharePreference().edit().clear().apply()
        user = null
    }

    fun setToken(token: String) {
        getUserSharePreference().edit().apply() {
            putString(PREF_TOKEN, token)
            apply()
        }
    }

    fun getToken(): String? {
        return getUserSharePreference().getString(PREF_TOKEN, null)
    }

    fun setLogin(login: Boolean) {
        getUserSharePreference().edit().apply() {
            putBoolean(LOGIN, login)
            apply()
        }
    }

    fun getLogin(): Boolean? {
        return getUserSharePreference().getBoolean(LOGIN, false)
    }

    fun setUser(user: com.example.qissa.models.User) {
        getUserSharePreference().edit().apply() {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val jsonAdapter = moshi.adapter(User::class.java)

            putString(PREF_USER, jsonAdapter.toJson(user))
            apply()
        }
    }

    fun getUser(): User? {
        return user ?: synchronized(this) {
            getUserSharePreference().let {
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                val jsonAdapter = moshi.adapter(User::class.java)
                val userJson = it.getString(PREF_USER, null)
                user = if (userJson == null) {
                    null
                } else {
                    jsonAdapter.fromJson(userJson)
                }

                user
            }
        }
    }

    fun setUserProfile(profile: Data) {

        Log.d("profile", profile.toString())
        getUserSharePreference().edit().apply() {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val jsonAdapter = moshi.adapter(Data::class.java)

            putString(PROFILE, jsonAdapter.toJson(profile))
            commit()
        }
    }

    fun getUsrProfile(): Data? {
        return profile ?: synchronized(this) {
            getUserSharePreference().let {
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                val jsonAdapter = moshi.adapter(Data::class.java)
                val userJson = it.getString(PROFILE, null)
                profile = if (userJson == null) {
                    null
                } else {
                    jsonAdapter.fromJson(userJson)
                }
                Log.d("usercode", "get--------" + profile.toString())

                profile
            }
        }
    }

    fun setImageUrl(pathImage: String) {
        getUserSharePreference().edit().apply() {
            putString(PATH_IMAGE, pathImage)
            apply()
        }
    }

    fun getImageUrl(): String? {
        return getUserSharePreference().getString(PATH_IMAGE, null)
    }

    fun setProfileNull() {
        getUserSharePreference().edit().remove(PROFILE).apply()
        profile = null
    }
}

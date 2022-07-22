package com.example.qissa.network

import com.example.qissa.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public class ApiClient {
    companion object {

        @Volatile
        private var retrofit: Retrofit? = null

        @Volatile
        private var apiInterface: ApiInterface? = null

        private fun buildClient(): OkHttpClient {
            return OkHttpClient.Builder().addInterceptor(
                HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                }
            ).addInterceptor { chain ->
                val request =
                    chain.request().newBuilder().addHeader("Accept", "application/json").build()
                chain.proceed(request)
            }.build()
        }

        @Synchronized
        private fun getRetrofit(): Retrofit {
            return retrofit ?: synchronized(this) {
                retrofit = Retrofit.Builder()
                    .client(buildClient())
                    .baseUrl(Constants.SERVER_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                return retrofit!!
            }
        }

        fun getApiInterface(): ApiInterface {
            return apiInterface ?: synchronized(this) {
                getRetrofit().create(ApiInterface::class.java)
            }
        }
    }
}

package com.example.qissa.di

import android.content.Context
import com.example.qissa.network.ApiClient
import com.example.qissa.network.ApiInterface
import com.example.qissa.utils.SharedPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideApiInterface(): ApiInterface {
        return ApiClient.getApiInterface()
    }

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreference {
        return SharedPreference(context)
    }
}

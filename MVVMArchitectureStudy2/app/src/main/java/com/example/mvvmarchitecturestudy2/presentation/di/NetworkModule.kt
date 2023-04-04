package com.example.mvvmarchitecturestudy2.presentation.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.example.mvvmarchitecturestudy2.BuildConfig
import com.example.mvvmarchitecturestudy2.data.api.TMDBAPIService
import com.example.mvvmarchitecturestudy2.data.util.NetworkManager

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideTMDBAPIService(retrofit: Retrofit) : TMDBAPIService {
        return retrofit.create(TMDBAPIService::class.java)
    }

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context) : Context {
        return context
    }

    @Singleton
    @Provides
    fun provideNetworkManager(context: Context) : NetworkManager {
        return NetworkManager(context)
    }
}
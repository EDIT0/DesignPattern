package com.my.mvistudymultimodule.core.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.core.util.NetworkStatusTracker
import com.my.mvistudymultimodule.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    class SetHeader() : Interceptor {
        @Throws(Exception::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {

            val newRequest = request().newBuilder()
                .addHeader("Accept", "*/*")
                .addHeader("Content-Type", "application/json")

            return proceed(newRequest.build())
        }
    }

    @Singleton
    @Provides
    fun provideSetHeader(): SetHeader {
        return SetHeader()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        setHeader: SetHeader
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                LogUtil.d_dev("[Network] ${message}")
            }
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(loggingInterceptor)
        }
        builder.connectTimeout(10000L, TimeUnit.SECONDS)
        builder.readTimeout(10000L, TimeUnit.SECONDS)
        builder.writeTimeout(10000L, TimeUnit.SECONDS)
        builder.addInterceptor(setHeader)

        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ) : Retrofit {
        val gson = GsonBuilder()
            .disableHtmlEscaping()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideAPIService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context) : Context {
        return context
    }

    @Singleton
    @Provides
    fun provideNetworkStatusTracker(context: Context) : NetworkStatusTracker {
        return NetworkStatusTracker(context)
    }
}
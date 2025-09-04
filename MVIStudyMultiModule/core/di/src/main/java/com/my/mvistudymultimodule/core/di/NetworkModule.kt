package com.my.mvistudymultimodule.core.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.core.util.NetworkStatusTracker
import com.my.mvistudymultimodule.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import javax.inject.Inject
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

    class AuthInterceptor @Inject constructor(
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            var token = TestToken.getToken() ?: ""

            // 첫 번째 요청 (토큰 포함)
            val requestWithToken = originalRequest.newBuilder()
                .addHeader("Accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer $token") // 토큰 추가하려고 임의로 넣어둔 것 (원래 필요없음)
                .build()

            LogUtil.d_dev("첫 번째 요청 Token: $token")

            var response = chain.proceed(requestWithToken)

            // 401 에러 시 토큰 갱신 후 재요청
//            if (response.code == 401) {
                synchronized(this) {
                    response.close() // 기존 response 닫기

                    val currentToken = TestToken.getToken() ?: ""

                    // 다른 스레드에서 이미 갱신했는지 확인
                    if (currentToken == token) {
                        LogUtil.d_dev("토큰 갱신 시작")
                        val newToken = TestToken.refreshToken()

                        // 토큰 리프래시 전용 Retrofit 생성
                        val tokenRefreshRetrofit = tokenRefreshRetrofit()
                        val tokenApi = tokenRefreshRetrofit.create(TokenRefreshApi::class.java)

                        val tokenApiResponse = runBlocking {
                            // 임의로 아무 api 사용하였음.
                            tokenApi.getPopularMovie(
                                language = "ko",
                                page = 1
                            )
                        }
                        LogUtil.d_dev("토큰 갱신 응답값: ${tokenApiResponse.body()}")
                        TestToken.saveToken(newToken)
                        token = newToken
                        LogUtil.d_dev("토큰 갱신 완료: $newToken")
                    } else {
                        token = currentToken
                        LogUtil.d_dev("다른 스레드에서 이미 갱신됨: $token")
                    }

                    // 새로운 토큰으로 재요청
                    val retryRequest = originalRequest.newBuilder()
                        .addHeader("Accept", "*/*")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer $token") // 토큰 추가하려고 임의로 넣어둔 것 (원래 필요없음)
                        .build()

                    LogUtil.d_dev("재요청 Token: $token")
                    response = chain.proceed(retryRequest)
//                }
            }

            return response
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        setHeader: SetHeader,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                LogUtil.d_dev("[Network] ${message}")
            }
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//            builder.addInterceptor(loggingInterceptor)
            builder.addNetworkInterceptor(loggingInterceptor) // 네트워크 레벨까지 다 찍히도록 변경
        }
        builder.connectTimeout(10000L, TimeUnit.SECONDS)
        builder.readTimeout(10000L, TimeUnit.SECONDS)
        builder.writeTimeout(10000L, TimeUnit.SECONDS)
//        builder.addInterceptor(setHeader) // 토큰 테스트 하지 않을 때 사용하는 것
        builder.addInterceptor(authInterceptor) // 토큰 테스트 할 때 사용하는 것

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

    /**
     * 리프래시용 레트로핏
     *
     * @return
     */
    private fun tokenRefreshRetrofit(): Retrofit {
        class SetHeader() : Interceptor {
            @Throws(Exception::class)
            override fun intercept(chain: Interceptor.Chain): Response = with(chain) {

                val newRequest = request().newBuilder()
                    .addHeader("Accept", "*/*")
                    .addHeader("Content-Type", "application/json")

                return proceed(newRequest.build())
            }
        }

        val setHeader = SetHeader()

        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                LogUtil.d_dev("[Refresh-Network] ${message}")
            }
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//            builder.addInterceptor(loggingInterceptor)
            builder.addNetworkInterceptor(loggingInterceptor) // 네트워크 레벨까지 다 찍히도록 변경
        }
        builder.connectTimeout(10000L, TimeUnit.SECONDS)
        builder.readTimeout(10000L, TimeUnit.SECONDS)
        builder.writeTimeout(10000L, TimeUnit.SECONDS)
        builder.addInterceptor(setHeader)

        val okHttpClient = builder.build()

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

    // 토큰 갱신 전용 API
    interface TokenRefreshApi {
        @GET("movie/upcoming")
        suspend fun getPopularMovie(
            @Query("api_key") api_key: String = com.my.mvistudymultimodule.data.BuildConfig.API_KEY,
            @Query("language") language: String = "ko-KR",
            @Query("page") page: Int,
            @Query("sort_by") sortBy: String = "popularity.desc",
            @Query("include_adult") include_adult: Boolean = false,
            @Query("include_video") include_video: Boolean = true
        ) : retrofit2.Response<MovieModel>
    }
}
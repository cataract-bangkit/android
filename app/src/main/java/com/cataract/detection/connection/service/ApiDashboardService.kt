package com.cataract.detection.connection.service

import android.content.Context
import com.cataract.detection.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiDashboardService(context: Context) {

    private val preferenceService = PreferencesService(context)

    private val httpClient = OkHttpClient.Builder().apply {
        addInterceptor(AuthInterceptor(preferenceService))
    }.build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private class AuthInterceptor(private val preferenceService: PreferencesService) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = preferenceService.getUserToken()
            val requestBuilder = chain.request().newBuilder()

            token?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }

            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }
}
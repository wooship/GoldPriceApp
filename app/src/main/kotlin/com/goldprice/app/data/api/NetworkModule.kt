package com.goldprice.app.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(GoldApiService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val goldApiService: GoldApiService by lazy {
        retrofit.create(GoldApiService::class.java)
    }

    private val metalpriceRetrofit = Retrofit.Builder()
        .baseUrl(MetalpriceApiService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val metalpriceApiService: MetalpriceApiService by lazy {
        metalpriceRetrofit.create(MetalpriceApiService::class.java)
    }

    private val goldApiComRetrofit = Retrofit.Builder()
        .baseUrl(GoldApiComService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val goldApiComService: GoldApiComService by lazy {
        goldApiComRetrofit.create(GoldApiComService::class.java)
    }

    private val exchangeRateRetrofit = Retrofit.Builder()
        .baseUrl(ExchangeRateApiService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val exchangeRateApiService: ExchangeRateApiService by lazy {
        exchangeRateRetrofit.create(ExchangeRateApiService::class.java)
    }
}

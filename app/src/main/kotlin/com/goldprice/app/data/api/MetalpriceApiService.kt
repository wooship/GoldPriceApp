package com.goldprice.app.data.api

import com.goldprice.app.data.model.MetalpriceConvertResponse
import com.goldprice.app.data.model.MetalpriceTimeframeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MetalpriceApiService {

    companion object {
        const val BASE_URL = "https://api.metalpriceapi.com/"
    }

    @GET("v1/latest")
    suspend fun getLatestPrice(
        @Query("api_key") apiKey: String,
        @Query("base") base: String = "XAU",
        @Query("currencies") currencies: String
    ): Response<MetalpriceTimeframeResponse>

    @GET("v1/timeframe")
    suspend fun getTimeframe(
        @Query("api_key") apiKey: String,
        @Query("base") base: String = "XAU",
        @Query("currencies") currencies: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Response<MetalpriceTimeframeResponse>

    @GET("v1/convert")
    suspend fun convertCurrency(
        @Query("api_key") apiKey: String,
        @Query("amount") amount: Double,
        @Query("from") from: String,
        @Query("to") to: String
    ): Response<MetalpriceConvertResponse>
}

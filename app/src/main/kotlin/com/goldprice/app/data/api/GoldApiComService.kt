package com.goldprice.app.data.api

import com.goldprice.app.data.model.GoldApiComErrorResponse
import com.goldprice.app.data.model.GoldApiComHistoryResponse
import com.goldprice.app.data.model.GoldApiComPriceResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GoldApiComService {

    companion object {
        const val BASE_URL = "https://api.gold-api.com/"
    }

    @GET("price/{symbol}")
    suspend fun getCurrentPrice(
        @Path("symbol") symbol: String,
        @Header("x-api-key") apiKey: String
    ): retrofit2.Response<GoldApiComPriceResponse>

    @GET("history/XAU")
    suspend fun getHistory(
        @Header("x-api-key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): retrofit2.Response<GoldApiComHistoryResponse>
}

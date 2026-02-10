package com.goldprice.app.data.api

import com.goldprice.app.data.model.GoldPriceResponse
import com.goldprice.app.data.model.HistoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GoldApiService {

    companion object {
        const val BASE_URL = "https://www.goldapi.io/api/"
    }

    @GET("XAU/{currency}")
    suspend fun getGoldPrice(
        @Header("x-access-token") token: String,
        @Path("currency") currency: String
    ): Response<GoldPriceResponse>

    @GET("XAU/{currency}/history")
    suspend fun getGoldHistory(
        @Header("x-access-token") token: String,
        @Path("currency") currency: String,
        @Query("date_from") dateFrom: String,
        @Query("date_to") dateTo: String
    ): Response<HistoryResponse>
}

package com.goldprice.app.data.api

import com.goldprice.app.data.model.ExchangeRateResponse
import retrofit2.http.GET

interface ExchangeRateApiService {

    companion object {
        const val BASE_URL = "https://api.exchangerate-api.com/"
    }

    @GET("v4/latest/USD")
    suspend fun getExchangeRates(): ExchangeRateResponse
}

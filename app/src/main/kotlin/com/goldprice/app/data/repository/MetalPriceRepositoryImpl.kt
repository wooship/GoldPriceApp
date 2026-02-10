package com.goldprice.app.data.repository

import com.goldprice.app.data.api.ExchangeRateApiService
import com.goldprice.app.data.api.GoldApiComService
import com.goldprice.app.data.model.GoldApiComPriceResponse
import com.goldprice.app.domain.model.CurrencyType
import com.goldprice.app.domain.model.MetalPrice
import com.goldprice.app.domain.model.MetalType
import com.goldprice.app.domain.repository.MetalPriceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class MetalPriceRepositoryImpl(
    private val goldApiComService: GoldApiComService,
    private val goldApiComApiKey: String,
    private val exchangeRateApiService: ExchangeRateApiService
) : MetalPriceRepository {

    companion object {
        private const val OUNCE_TO_GRAM = 31.1035
    }

    override suspend fun getMetalPrice(
        metalType: MetalType,
        currencyType: CurrencyType
    ): Result<MetalPrice> = withContext(Dispatchers.IO) {
        try {
            val symbol = if (metalType == MetalType.GOLD) "XAU" else "XAG"
            android.util.Log.d("MetalPrice", "Fetching $symbol price in $currencyType")

            val priceResponse = goldApiComService.getCurrentPrice(
                symbol = symbol,
                apiKey = goldApiComApiKey
            )

            if (priceResponse.isSuccessful && priceResponse.body() != null) {
                val priceBody = priceResponse.body()!!
                val currentPriceUsdPerOunce = priceBody.price
                val timestamp = System.currentTimeMillis()

                android.util.Log.d("MetalPrice", "$metalType USD price: $currentPriceUsdPerOunce, timestamp: $timestamp")

                val finalPrice = if (currencyType == CurrencyType.USD) {
                    currentPriceUsdPerOunce
                } else {
                    val exchangeRateResponse = exchangeRateApiService.getExchangeRates()
                    val usdToCnyRate = exchangeRateResponse.rates["CNY"]
                        ?: return@withContext Result.failure(Exception("无法获取汇率"))
                    (currentPriceUsdPerOunce * usdToCnyRate) / OUNCE_TO_GRAM
                }

                android.util.Log.d("MetalPrice", "$metalType $currencyType price: $finalPrice")

                val bid = finalPrice * 0.995
                val ask = finalPrice * 1.005

                Result.success(
                    MetalPrice(
                        metalType = metalType,
                        currency = currencyType,
                        price = finalPrice,
                        bid = bid,
                        ask = ask,
                        timestamp = timestamp
                    )
                )
            } else {
                val errorMsg = "API error: ${priceResponse.code()} - ${priceResponse.message()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            android.util.Log.e("MetalPrice", "Error fetching $metalType price", e)
            Result.failure(e)
        }
    }
}

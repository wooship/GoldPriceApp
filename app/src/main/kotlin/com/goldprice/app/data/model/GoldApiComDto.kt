package com.goldprice.app.data.model

/**
 * Gold-API.com current price response
 * GET https://api.gold-api.com/price/XAU
 */
data class GoldApiComPriceResponse(
    val name: String,
    val price: Double,
    val symbol: String,
    val updatedAt: String,
    val updatedAtReadable: String
)

/**
 * Gold-API.com historical response
 * GET https://api.gold-api.com/history/XAU?start_date=2026-02-05&end_date=2026-02-09
 */
data class GoldApiComHistoryResponse(
    val symbol: String,
    val name: String,
    val currency: String?,
    val prices: Map<String, Double>?  // Map<Date, Price>
)

/**
 * Gold-API.com error response
 */
data class GoldApiComErrorResponse(
    val error: String
)

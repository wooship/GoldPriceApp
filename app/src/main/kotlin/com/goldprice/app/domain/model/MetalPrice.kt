package com.goldprice.app.domain.model

enum class MetalType {
    GOLD,
    SILVER
}

enum class CurrencyType {
    USD,
    CNY
}

data class MetalPrice(
    val metalType: MetalType,
    val currency: CurrencyType,
    val price: Double,
    val bid: Double,
    val ask: Double,
    val timestamp: Long,
    val changeAmount: Double? = null,
    val changePercent: Double? = null
)

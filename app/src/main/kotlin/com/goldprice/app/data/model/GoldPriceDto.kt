package com.goldprice.app.data.model

import com.google.gson.annotations.SerializedName

data class GoldPriceResponse(
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("metal")
    val metal: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("bid")
    val bid: Double,
    @SerializedName("ask")
    val ask: Double,
    @SerializedName("ch")
    val change: Double?,
    @SerializedName("chp")
    val changePercent: Double?
)

data class HistoryResponse(
    @SerializedName("items")
    val items: List<HistoryItem>?
)

data class HistoryItem(
    @SerializedName("date")
    val date: String,
    @SerializedName("price")
    val price: Double
)

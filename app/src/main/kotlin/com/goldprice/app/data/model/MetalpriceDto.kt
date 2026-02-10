package com.goldprice.app.data.model

import com.google.gson.annotations.SerializedName

data class MetalpriceTimeframeResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Long?,
    @SerializedName("base")
    val base: String?,
    @SerializedName("rates")
    val rates: Any?,
    @SerializedName("unit")
    val unit: String?,
    @SerializedName("start_date")
    val startDate: String?,
    @SerializedName("end_date")
    val endDate: String?,
    @SerializedName("error")
    val error: Map<String, Any>? = null
) {
    fun getRatesAsMap(): Map<String, Double>? {
        return when (rates) {
            is Map<*, *> -> rates as? Map<String, Double>
            else -> null
        }
    }
}

data class MetalpriceConvertResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("query")
    val query: ConvertQuery?,
    @SerializedName("info")
    val info: ConvertInfo?,
    @SerializedName("result")
    val result: Double?,
    @SerializedName("error")
    val error: Map<String, Any>? = null
)

data class ConvertQuery(
    @SerializedName("from")
    val from: String?,
    @SerializedName("to")
    val to: String?,
    @SerializedName("amount")
    val amount: Double?
)

data class ConvertInfo(
    @SerializedName("quote")
    val quote: Double?,
    @SerializedName("timestamp")
    val timestamp: Long?
)

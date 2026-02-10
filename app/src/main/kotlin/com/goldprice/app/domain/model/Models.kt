package com.goldprice.app.domain.model

/**
 * 金价数据领域模型
 */
data class GoldPrice(
    val symbol: String,           // 金属符号 (XAU)
    val currency: String,         // 货币代码 (USD/CNY)
    val price: Double,            // 当前价格
    val bid: Double,              // 买入价
    val ask: Double,              // 卖出价
    val timestamp: Long,          // 时间戳
    val changePercent: Double? = null, // 涨跌幅
    val changeAmount: Double? = null   // 涨跌额
)

/**
 * 历史数据点，用于走势图
 */
data class HistoryPoint(
    val timestamp: Long,
    val price: Double
)

/**
 * 金价类型
 */
enum class GoldPriceType {
    INTERNATIONAL,  // 国际金价 (USD)
    DOMESTIC        // 国内金价 (CNY)
}

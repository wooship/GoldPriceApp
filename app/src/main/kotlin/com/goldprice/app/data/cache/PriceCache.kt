package com.goldprice.app.data.cache

import android.content.Context
import android.content.SharedPreferences
import com.goldprice.app.domain.model.CurrencyType
import com.goldprice.app.domain.model.MetalPrice
import com.goldprice.app.domain.model.MetalType

class PriceCache(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("PriceCache", Context.MODE_PRIVATE)

    private fun getKey(metalType: MetalType, currencyType: CurrencyType, field: String): String {
        return "${metalType.name}_${currencyType.name}_$field"
    }

    fun savePrice(price: MetalPrice) {
        val editor = prefs.edit()
        
        editor.putFloat(getKey(price.metalType, price.currency, "price"), price.price.toFloat())
        editor.putFloat(getKey(price.metalType, price.currency, "bid"), price.bid.toFloat())
        editor.putFloat(getKey(price.metalType, price.currency, "ask"), price.ask.toFloat())
        editor.putLong(getKey(price.metalType, price.currency, "timestamp"), price.timestamp)
        
        editor.apply()
        
        android.util.Log.d("PriceCache", "Saved ${price.metalType} ${price.currency}: ${price.price}")
    }

    fun loadPrice(metalType: MetalType, currencyType: CurrencyType): MetalPrice? {
        val price = prefs.getFloat(getKey(metalType, currencyType, "price"), 0f)
        
        if (price > 0f) {
            val result = MetalPrice(
                metalType = metalType,
                currency = currencyType,
                price = price.toDouble(),
                bid = prefs.getFloat(getKey(metalType, currencyType, "bid"), 0f).toDouble(),
                ask = prefs.getFloat(getKey(metalType, currencyType, "ask"), 0f).toDouble(),
                timestamp = prefs.getLong(getKey(metalType, currencyType, "timestamp"), 0L)
            )
            android.util.Log.d("PriceCache", "Loaded ${metalType} ${currencyType}: $price")
            return result
        }
        
        android.util.Log.d("PriceCache", "No cached data for ${metalType} ${currencyType}")
        return null
    }
}

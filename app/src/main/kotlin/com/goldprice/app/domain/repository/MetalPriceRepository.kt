package com.goldprice.app.domain.repository

import com.goldprice.app.domain.model.CurrencyType
import com.goldprice.app.domain.model.MetalPrice
import com.goldprice.app.domain.model.MetalType

interface MetalPriceRepository {
    suspend fun getMetalPrice(
        metalType: MetalType,
        currencyType: CurrencyType
    ): Result<MetalPrice>
}

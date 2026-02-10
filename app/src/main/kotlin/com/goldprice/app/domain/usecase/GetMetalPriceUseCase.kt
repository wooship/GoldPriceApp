package com.goldprice.app.domain.usecase

import com.goldprice.app.domain.model.CurrencyType
import com.goldprice.app.domain.model.MetalPrice
import com.goldprice.app.domain.model.MetalType
import com.goldprice.app.domain.repository.MetalPriceRepository

class GetMetalPriceUseCase(
    private val repository: MetalPriceRepository
) {
    suspend operator fun invoke(
        metalType: MetalType,
        currencyType: CurrencyType
    ): Result<MetalPrice> = repository.getMetalPrice(metalType, currencyType)
}

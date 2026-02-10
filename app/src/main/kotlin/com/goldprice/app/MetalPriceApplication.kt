package com.goldprice.app

import android.app.Application
import com.goldprice.app.data.api.NetworkModule
import com.goldprice.app.data.repository.MetalPriceRepositoryImpl
import com.goldprice.app.domain.repository.MetalPriceRepository
import com.goldprice.app.domain.usecase.GetMetalPriceUseCase

class MetalPriceApplication : Application() {

    lateinit var repository: MetalPriceRepository
        private set

    lateinit var getMetalPriceUseCase: GetMetalPriceUseCase
        private set

    private val goldApiComApiKey = "efaa3b07b4ff91e768d16e08a9fd152a32af5e9bb57779100297fe8942f83fc7"

    override fun onCreate() {
        super.onCreate()
        initDependencies()
    }

    private fun initDependencies() {
        val goldApiComService = NetworkModule.goldApiComService
        val exchangeRateApiService = NetworkModule.exchangeRateApiService
        repository = MetalPriceRepositoryImpl(goldApiComService, goldApiComApiKey, exchangeRateApiService)

        getMetalPriceUseCase = GetMetalPriceUseCase(repository)
    }
}

package com.goldprice.app.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import com.goldprice.app.data.cache.PriceCache
import com.goldprice.app.domain.model.CurrencyType
import com.goldprice.app.domain.model.MetalPrice
import com.goldprice.app.domain.model.MetalType
import com.goldprice.app.domain.usecase.GetMetalPriceUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime

data class MetalPriceUiState(
    val goldUsdPrice: MetalPrice? = null,
    val goldCnyPrice: MetalPrice? = null,
    val silverUsdPrice: MetalPrice? = null,
    val silverCnyPrice: MetalPrice? = null,
    val error: String? = null,
    val lastUpdateTime: Long = 0L,
    val isRefreshing: Boolean = false
) {
    val hasAnyPrice: Boolean
        get() = goldUsdPrice != null || goldCnyPrice != null || silverUsdPrice != null || silverCnyPrice != null
}

class MetalPriceViewModel(
    private val getMetalPriceUseCase: GetMetalPriceUseCase,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MetalPriceUiState())
    val uiState: StateFlow<MetalPriceUiState> = _uiState.asStateFlow()

    private val priceCache = PriceCache(context)

    private val CHINA_TIMEZONE = ZoneId.of("Asia/Shanghai")

    init {
        loadFromCache()
        loadAllPrices()
        startAutoRefresh()
    }

    private fun loadFromCache() {
        viewModelScope.launch {
            val goldUsd = priceCache.loadPrice(MetalType.GOLD, CurrencyType.USD)
            val goldCny = priceCache.loadPrice(MetalType.GOLD, CurrencyType.CNY)
            val silverUsd = priceCache.loadPrice(MetalType.SILVER, CurrencyType.USD)
            val silverCny = priceCache.loadPrice(MetalType.SILVER, CurrencyType.CNY)

            _uiState.update { currentState ->
                currentState.copy(
                    goldUsdPrice = goldUsd ?: currentState.goldUsdPrice,
                    goldCnyPrice = goldCny ?: currentState.goldCnyPrice,
                    silverUsdPrice = silverUsd ?: currentState.silverUsdPrice,
                    silverCnyPrice = silverCny ?: currentState.silverCnyPrice,
                    lastUpdateTime = if (goldUsd != null) goldUsd.timestamp else currentState.lastUpdateTime
                )
            }
        }
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                android.util.Log.d("MetalPriceViewModel", "Auto refresh trigger ${System.currentTimeMillis()}")
                delay(60000)
                loadAllPrices()
            }
        }
    }

    fun loadAllPrices() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            android.util.Log.d("MetalPriceViewModel", "Loading all prices at ${System.currentTimeMillis()}")
            val goldUsdResult = getMetalPriceUseCase(MetalType.GOLD, CurrencyType.USD)
            val goldCnyResult = getMetalPriceUseCase(MetalType.GOLD, CurrencyType.CNY)
            val silverUsdResult = getMetalPriceUseCase(MetalType.SILVER, CurrencyType.USD)
            val silverCnyResult = getMetalPriceUseCase(MetalType.SILVER, CurrencyType.CNY)

            val newGoldUsd = goldUsdResult.getOrNull()
            val newGoldCny = goldCnyResult.getOrNull()
            val newSilverUsd = silverUsdResult.getOrNull()
            val newSilverCny = silverCnyResult.getOrNull()

            val currentState = _uiState.value

            val newState = currentState.copy(
                goldUsdPrice = newGoldUsd ?: currentState.goldUsdPrice,
                goldCnyPrice = newGoldCny ?: currentState.goldCnyPrice,
                silverUsdPrice = newSilverUsd ?: currentState.silverUsdPrice,
                silverCnyPrice = newSilverCny ?: currentState.silverCnyPrice,
                lastUpdateTime = if (newGoldUsd != null || newGoldCny != null || newSilverUsd != null || newSilverCny != null) System.currentTimeMillis() else currentState.lastUpdateTime,
                isRefreshing = false
            )

            val errors = listOfNotNull(
                goldUsdResult.exceptionOrNull()?.message,
                goldCnyResult.exceptionOrNull()?.message,
                silverUsdResult.exceptionOrNull()?.message,
                silverCnyResult.exceptionOrNull()?.message
            )

            val finalState = if (errors.isNotEmpty() && !newState.hasAnyPrice) {
                newState.copy(error = "加载失败: ${errors.joinToString("; ")}")
            } else {
                newState.copy(error = null)
            }

            _uiState.update { finalState }

            newGoldUsd?.let { priceCache.savePrice(it) }
            newGoldCny?.let { priceCache.savePrice(it) }
            newSilverUsd?.let { priceCache.savePrice(it) }
            newSilverCny?.let { priceCache.savePrice(it) }
        }
    }

    fun getFormattedUpdateTime(): String {
        val time = ZonedDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(_uiState.value.lastUpdateTime),
            CHINA_TIMEZONE
        )
        return time.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    class Factory(
        private val getMetalPriceUseCase: GetMetalPriceUseCase,
        private val context: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MetalPriceViewModel::class.java)) {
                return MetalPriceViewModel(getMetalPriceUseCase, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

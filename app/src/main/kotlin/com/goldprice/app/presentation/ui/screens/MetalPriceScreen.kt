package com.goldprice.app.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.goldprice.app.presentation.ui.components.MetalPriceCard
import com.goldprice.app.presentation.viewmodel.MetalPriceUiState
import com.goldprice.app.presentation.viewmodel.MetalPriceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetalPriceScreen(
    viewModel: MetalPriceViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadAllPrices()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadAllPrices()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "实时金银",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MetalPriceContent(uiState = uiState)
        }
    }
}

@Composable
private fun MetalPriceContent(
    uiState: MetalPriceUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MetalPriceCard(
                metalType = com.goldprice.app.domain.model.MetalType.GOLD,
                currencyType = com.goldprice.app.domain.model.CurrencyType.USD,
                price = uiState.goldUsdPrice
            )
        }

        item {
            MetalPriceCard(
                metalType = com.goldprice.app.domain.model.MetalType.GOLD,
                currencyType = com.goldprice.app.domain.model.CurrencyType.CNY,
                price = uiState.goldCnyPrice
            )
        }

        item {
            MetalPriceCard(
                metalType = com.goldprice.app.domain.model.MetalType.SILVER,
                currencyType = com.goldprice.app.domain.model.CurrencyType.USD,
                price = uiState.silverUsdPrice
            )
        }

        item {
            MetalPriceCard(
                metalType = com.goldprice.app.domain.model.MetalType.SILVER,
                currencyType = com.goldprice.app.domain.model.CurrencyType.CNY,
                price = uiState.silverCnyPrice
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

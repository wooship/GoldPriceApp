package com.goldprice.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.goldprice.app.presentation.ui.screens.MetalPriceScreen
import com.goldprice.app.presentation.ui.theme.GoldPriceAppTheme
import com.goldprice.app.presentation.viewmodel.MetalPriceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as MetalPriceApplication

        setContent {
            GoldPriceAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MetalPriceViewModel = viewModel(
                        factory = MetalPriceViewModel.Factory(
                            getMetalPriceUseCase = app.getMetalPriceUseCase,
                            context = this
                        )
                    )
                    MetalPriceScreen(viewModel = viewModel)
                }
            }
        }
    }
}

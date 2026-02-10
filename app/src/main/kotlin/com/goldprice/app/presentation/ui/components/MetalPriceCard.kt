package com.goldprice.app.presentation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.goldprice.app.domain.model.CurrencyType
import com.goldprice.app.domain.model.MetalPrice
import com.goldprice.app.domain.model.MetalType
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MetalPriceCard(
    metalType: MetalType,
    currencyType: CurrencyType,
    price: MetalPrice?,
    modifier: Modifier = Modifier
) {
    val (titleText, unitText) = when {
        metalType == MetalType.GOLD && currencyType == CurrencyType.USD -> Pair("国际金价", "美元/盎司")
        metalType == MetalType.GOLD && currencyType == CurrencyType.CNY -> Pair("国内金价", "人民币/克")
        metalType == MetalType.SILVER && currencyType == CurrencyType.USD -> Pair("国际银价", "美元/盎司")
        metalType == MetalType.SILVER && currencyType == CurrencyType.CNY -> Pair("国内银价", "人民币/克")
        else -> Pair("", "")
    }

    val metalColor = when (metalType) {
        MetalType.GOLD -> Color(0xFFFFD700)
        MetalType.SILVER -> Color(0xFFC0C0C0)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "$titleText ($unitText)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (price != null) {
                val currencySymbol = if (price.currency == CurrencyType.CNY) "¥" else "$"
                val priceText = DecimalFormat("#,##0.00").format(price.price)
                val bidAskFormat = DecimalFormat("#,##0.00")

                AnimatedClockValue(
                    value = priceText,
                    prefix = currencySymbol,
                    textColor = metalColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "买入: $currencySymbol ${bidAskFormat.format(price.bid)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "卖出: $currencySymbol ${bidAskFormat.format(price.ask)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val updateTime = formatTimestampChina(price.timestamp)
                Text(
                    text = "更新时间: $updateTime",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            } else {
                val currencySymbol = if (currencyType == CurrencyType.CNY) "¥" else "$"
                
                Text(
                    text = "$currencySymbol ______.__",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = metalColor.copy(alpha = 0.3f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "买入: $currencySymbol ____.__",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray.copy(alpha = 0.3f)
                    )
                    Text(
                        text = "卖出: $currencySymbol ____.__",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray.copy(alpha = 0.3f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "更新时间: __:__:__",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
fun AnimatedClockValue(
    value: String,
    prefix: String,
    textColor: Color
) {
    AnimatedContent(
        targetState = value,
        label = "clock_value",
        transitionSpec = {
            slideInVertically { height -> height } + fadeIn() togetherWith
            slideOutVertically { height -> -height } + fadeOut()
        }
    ) { targetValue ->
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = prefix,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = targetValue,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

private fun formatTimestampChina(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.CHINA)
    return sdf.format(Date(timestamp))
}

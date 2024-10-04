package com.example.catandmouse.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catandmouse.ui.GameViewModel

@Composable
fun StartScreen(
    viewModel: GameViewModel,
    onStartGame: () -> Unit,
    onShowScore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.gameUiState.collectAsState()
    val mouseCount = uiState.mouseCount
    val mouseSpeed = uiState.mouseSpeed
    val mouseSize = uiState.mouseSize
    val context = LocalContext.current
    var isVibrationEnabled by remember { mutableStateOf(isVibrationEnabled(context)) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Настройки игры",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Количество мышек: $mouseCount",
        )
        Slider(
            value = mouseCount.toFloat(),
            onValueChange = { viewModel.setMouseCount(it.toInt()) },
            valueRange = 1f..5f,
            modifier = Modifier.widthIn(max = 400.dp)
        )

        Text("Скорость мышек: ${mouseSpeed.toInt()}")
        Slider(
            value = mouseSpeed,
            onValueChange = { viewModel.setMouseSpeed(it) },
            valueRange = 1f..10f,
            modifier = Modifier.widthIn(max = 400.dp)
        )

        Text("Размер мышек: ${mouseSize.value.toInt()}")
        Slider(
            value = mouseSize.value,
            onValueChange = { viewModel.setMouseSize(it.dp) },
            valueRange = 50f..300f,
            modifier = Modifier.widthIn(max = 400.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Использовать вибрацию")
            Checkbox(
                checked = isVibrationEnabled,
                onCheckedChange = { isChecked ->
                    isVibrationEnabled = isChecked
                    setVibrationEnabled(context, isChecked)
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(128.dp)
        ) {
            Button(
                onClick = onStartGame,
                modifier = Modifier.fillMaxWidth()

            ) {
                Text(
                    text = "Старт",
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = onShowScore,
                modifier = Modifier.fillMaxWidth()

            ) {
                Text(
                    text = "Счёт",
                )
            }
        }
    }
}


fun setVibrationEnabled(context: Context, isEnabled: Boolean) {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putBoolean("vibration_enabled", isEnabled)
        apply()
    }
}

fun isVibrationEnabled(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("vibration_enabled", true)
}

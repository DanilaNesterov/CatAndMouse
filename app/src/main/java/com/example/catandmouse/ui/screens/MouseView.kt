package com.example.catandmouse.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import com.example.catandmouse.R
import com.example.catandmouse.data.Mouse

@Composable
fun MouseView(
    mouse: Mouse,
    onMouseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .offset { IntOffset(mouse.x.toInt(), mouse.y.toInt()) }
            .size(mouse.size)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                triggerVibration(context)
                onMouseClick()
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.mouse),
            contentDescription = stringResource(R.string.mouse),
            modifier = Modifier.fillMaxSize()
        )
    }
}


fun triggerVibration(context: Context) {
    if (!isVibrationEnabled(context)) return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(VibratorManager::class.java)
        val vibrator = vibratorManager.defaultVibrator
        val effect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(effect)
    } else {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        } else {
            vibrator.vibrate(100)
        }
    }
}
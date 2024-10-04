package com.example.catandmouse.data

import androidx.compose.ui.unit.Dp

data class Mouse(
    val id: Int,
    var x: Float,
    var y: Float,
    var speed: Float,
    var dx: Float = 1f,
    var dy: Float = 1f,
    var stepsCount: Int = 0,
    val maxSteps: Int = (50..200).random(),
    val size: Dp
)
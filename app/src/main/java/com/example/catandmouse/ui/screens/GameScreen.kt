package com.example.catandmouse.ui.screens

import android.media.MediaPlayer
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catandmouse.R
import com.example.catandmouse.ui.GameViewModel
import kotlinx.coroutines.delay


@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.gameUiState.collectAsState()
    val context = LocalContext.current

    BackHandler {
        onStop()
    }

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    LaunchedEffect(uiState.isGameRunning) {
        if (uiState.isGameRunning) {
            mediaPlayer = MediaPlayer.create(context, R.raw.mouse_sound)

            while (uiState.isGameRunning) {
                mediaPlayer?.start()
                delay((10_000).toLong())
            }
        } else {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                viewModel.onClick()
            }
    ) {

        val parentWidth = constraints.maxWidth.toFloat()
        val parentHeight = constraints.maxHeight.toFloat()
        val mouseSizePx = with(LocalDensity.current) { uiState.mouseSize.toPx() }

        LaunchedEffect(uiState.isGameRunning) {
            if (uiState.isGameRunning) {
                viewModel.initializeMice(uiState.mouseCount, parentWidth, parentHeight, mouseSizePx)

                while (uiState.isGameRunning) {
                    delay(16)
                    viewModel.updateMicePositions(parentWidth, parentHeight, mouseSizePx)

                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = onStop,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .size(48.dp)

        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Stop",
                tint = Color.White,
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = viewModel.formatTime(uiState.gameDuration),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            uiState.mice.forEach { mouse ->
                MouseView(
                    mouse = mouse,
                    onMouseClick = { viewModel.onMouseClick() }
                )
            }
        }

        Text(
            text = "${uiState.score}",
            color = Color.White,
            fontSize = 32.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
        )

    }
}
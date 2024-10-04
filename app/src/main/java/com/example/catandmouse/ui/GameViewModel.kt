package com.example.catandmouse.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catandmouse.data.GameStat
import com.example.catandmouse.data.GameStatRepository
import com.example.catandmouse.data.Mouse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameStatRepository: GameStatRepository
) : ViewModel() {
    private val _gameUiState: MutableStateFlow<GameUiState> = MutableStateFlow(GameUiState())
    val gameUiState: StateFlow<GameUiState> = _gameUiState.asStateFlow()

    private val _gameStats: MutableStateFlow<List<GameStat>> =
        MutableStateFlow(emptyList())
    val gameStats: StateFlow<List<GameStat>> = _gameStats.asStateFlow()

    init {
        getAllGameStat()
    }

    fun addGameStat(gameStat: GameStat) {
        viewModelScope.launch {
            gameStatRepository.addGameStat(gameStat)
        }
    }

    fun removeGameStat(gameStat: GameStat) {
        viewModelScope.launch {
            gameStatRepository.removeGameState(gameStat)
        }
    }

    fun getAllGameStat() {
        viewModelScope.launch {
            gameStatRepository.getAllGameStat().collect() { gameStats ->
                _gameStats.value = gameStats
            }
        }
    }

    fun onClick() {
        _gameUiState.update { currentUiState ->
            currentUiState.copy(
                totalTaps = currentUiState.totalTaps.inc()
            )
        }
    }

    fun onMouseClick() {
        _gameUiState.update { currentUiState ->
            currentUiState.copy(
                totalTaps = currentUiState.totalTaps.inc(),
                score = currentUiState.score.inc()
            )
        }
    }

    fun initializeMice(
        mouseCount: Int,
        screenWidth: Float,
        screenHeight: Float,
        mouseSizePx: Float
    ) {
        val mouseSpeed = _gameUiState.value.mouseSpeed

        _gameUiState.update { currentUiState ->
            currentUiState.copy(
                mice = List(mouseCount) { id ->
                    val x = (Math.random() * (screenWidth - mouseSizePx)).toFloat()
                    val y = (Math.random() * (screenHeight - mouseSizePx)).toFloat()

                    Mouse(
                        id = id,
                        x = x,
                        y = y,
                        speed = mouseSpeed,
                        size = _gameUiState.value.mouseSize
                    )
                }
            )
        }
    }

    fun updateMicePositions(screenWidth: Float, screenHeight: Float, mouseSizePx: Float) {
        val updatedMicePositions = _gameUiState.value.mice.map { mouse ->
            var newX = mouse.x + mouse.dx * mouse.speed
            var newY = mouse.y + mouse.dy * mouse.speed
            var newDx = mouse.dx
            var newDy = mouse.dy

            if (newX <= 0) {
                newX = 0f
                newDx = -newDx
            } else if (newX + mouseSizePx >= screenWidth) {
                newX = screenWidth - mouseSizePx
                newDx = -newDx
            }

            if (newY <= 0) {
                newY = 0f
                newDy = -newDy
            } else if (newY + mouseSizePx >= screenHeight) {
                newY = screenHeight - mouseSizePx
                newDy = -newDy
            }

            val stepsCount = mouse.stepsCount + 1
            if (stepsCount >= mouse.maxSteps) {
                val directions = listOf(
                    Pair(-1f, 0f), Pair(1f, 0f),
                    Pair(0f, -1f), Pair(0f, 1f),
                    Pair(-1f, -1f), Pair(1f, 1f),
                    Pair(1f, -1f), Pair(-1f, 1f)
                )
                val (newRandomDx, newRandomDy) = directions.random()
                newDx = newRandomDx
                newDy = newRandomDy
            }

            mouse.copy(
                x = newX,
                y = newY,
                dx = newDx,
                dy = newDy,
                stepsCount = if (stepsCount >= mouse.maxSteps) 0 else stepsCount
            )
        }

        _gameUiState.update { currentUiState ->
            currentUiState.copy(mice = updatedMicePositions)
        }
    }

    fun setMouseCount(count: Int) {
        _gameUiState.update { currentUiState ->
            currentUiState.copy(mouseCount = count)
        }
    }

    fun setMouseSize(size: Dp) {
        _gameUiState.update { currentUiState ->
            currentUiState.copy(mouseSize = size)
        }
    }

    fun setMouseSpeed(speed: Float) {
        _gameUiState.update { currentUiState ->
            currentUiState.copy(mouseSpeed = speed)
        }
    }

    fun stop() {
        val totalTaps = _gameUiState.value.totalTaps
        val successfulTaps = _gameUiState.value.score
        addGameStat(
            GameStat(
                totalTaps = totalTaps,
                successfulTaps = successfulTaps,
                accuracy = calculateAccuracy(totalTaps = totalTaps, successfulTaps = successfulTaps),
                gameDuration = formatTime(_gameUiState.value.gameDuration)
            )
        )
        _gameUiState.update { currentUiState ->
            currentUiState.copy(
                isGameRunning = false,
                mice = emptyList(),
                score = 0,
                totalTaps = 0,
                gameDuration = 0
            )
        }
    }

    fun start() {
        _gameUiState.update { currentUiState ->
            currentUiState.copy(
                isGameRunning = true,
                gameDuration = 0
            )
        }
        startTimer()
    }

    fun calculateAccuracy(totalTaps: Int, successfulTaps: Int): Int {
        if (successfulTaps > 0) {
            return ((successfulTaps.toFloat() / totalTaps.toFloat()) * 100f).toInt()
        } else {
            return 0
        }
    }

    fun startTimer() {
        viewModelScope.launch {
            while (_gameUiState.value.isGameRunning) {
                delay(1000L)
                _gameUiState.update { currentUiState ->
                    currentUiState.copy(
                        gameDuration = currentUiState.gameDuration.inc()
                    )
                }
            }
        }
    }

    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }
}

data class GameUiState(
    val isGameRunning: Boolean = false,
    val mouseSize: Dp = 50.dp,
    val mouseCount: Int = 1,
    val mouseSpeed: Float = 1f,
    val mice: List<Mouse> = emptyList(),
    val totalTaps: Int = 0,
    val score: Int = 0,
    val gameDuration: Int = 0
)



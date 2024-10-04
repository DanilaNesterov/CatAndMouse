package com.example.catandmouse

import androidx.compose.ui.unit.dp
import com.example.catandmouse.data.GameStat
import com.example.catandmouse.data.GameStatRepository
import com.example.catandmouse.ui.GameViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    private lateinit var viewModel: GameViewModel
    private lateinit var gameStatRepository: GameStatRepository
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setUp() {

        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        gameStatRepository = mockk(relaxed = true)

        coEvery { gameStatRepository.getAllGameStat() } returns flowOf(emptyList())

        viewModel = GameViewModel(gameStatRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun testInitialState() {
        val state = viewModel.gameUiState.value
        assertFalse(state.isGameRunning)
        assertEquals(50.dp, state.mouseSize)
        assertEquals(1, state.mouseCount)
        assertEquals(1f, state.mouseSpeed, 0.01f)
        assertTrue(state.mice.isEmpty())
        assertEquals(0, state.totalTaps)
        assertEquals(0, state.score)
        assertEquals(0, state.gameDuration)
    }

    @Test
    fun testOnClickIncrementsTotalTaps() {
        viewModel.onClick()
        assertEquals(1, viewModel.gameUiState.value.totalTaps)

        viewModel.onClick()
        assertEquals(2, viewModel.gameUiState.value.totalTaps)
    }

    @Test
    fun testOnMouseClickIncrementsScore() {
        viewModel.onMouseClick()
        assertEquals(1, viewModel.gameUiState.value.score)

        viewModel.onMouseClick()
        assertEquals(2, viewModel.gameUiState.value.score)
        assertEquals(2, viewModel.gameUiState.value.totalTaps)
    }

    @Test
    fun testInitializeMice() {
        viewModel.initializeMice(5, 500f, 500f, 50f)

        assertEquals(5, viewModel.gameUiState.value.mice.size)
    }

    @Test
    fun testUpdateMicePositions() {
        viewModel.initializeMice(2, 500f, 500f, 50f)
        val initialPositions = viewModel.gameUiState.value.mice.map { it.x to it.y }

        viewModel.updateMicePositions(500f, 500f, 50f)
        val updatedPositions = viewModel.gameUiState.value.mice.map { it.x to it.y }

        assertNotEquals(initialPositions, updatedPositions)
    }

    @Test
    fun testSetMouseCount() {
        viewModel.setMouseCount(3)
        assertEquals(3, viewModel.gameUiState.value.mouseCount)
    }

    @Test
    fun testSetMouseSize() {
        viewModel.setMouseSize(100.dp)
        assertEquals(100.dp, viewModel.gameUiState.value.mouseSize)
    }

    @Test
    fun testSetMouseSpeed() {
        viewModel.setMouseSpeed(5f)
        assertEquals(5f, viewModel.gameUiState.value.mouseSpeed, 0.01f)
    }

    @Test
    fun testStartAndStopGame() = runTest {
        viewModel.start()
        assertTrue(viewModel.gameUiState.value.isGameRunning)

        viewModel.stop()
        assertFalse(viewModel.gameUiState.value.isGameRunning)
        assertEquals(0, viewModel.gameUiState.value.score)
        assertEquals(0, viewModel.gameUiState.value.totalTaps)
    }

    @Test
    fun testFormatTime() {
        val formattedTime = viewModel.formatTime(125)
        assertEquals("02:05", formattedTime)
    }

    @Test
    fun testCalculateAccuracy() {
        val accuracy = viewModel.calculateAccuracy(10, 5)
        assertEquals(50, accuracy)

        val accuracyZero = viewModel.calculateAccuracy(0, 0)
        assertEquals(0, accuracyZero)
    }

    @Test
    fun testAddGameStat() = runTest {
        val gameStat =
            GameStat(totalTaps = 10, successfulTaps = 5, accuracy = 50, gameDuration = "00:10")

        viewModel.addGameStat(gameStat)

        advanceUntilIdle()

        coVerify { gameStatRepository.addGameStat(gameStat) }
    }

    @Test
    fun testRemoveGameStat() = runTest {
        val gameStat =
            GameStat(totalTaps = 10, successfulTaps = 5, accuracy = 50, gameDuration = "00:10")

        viewModel.removeGameStat(gameStat)
        advanceUntilIdle()

        coVerify { gameStatRepository.removeGameState(gameStat) }
    }

    @Test
    fun testGetAllGameStat(): Unit = runTest {
        val stats = listOf(
            GameStat(totalTaps = 10, successfulTaps = 5, accuracy = 50, gameDuration = "00:10"),
            GameStat(totalTaps = 20, successfulTaps = 10, accuracy = 50, gameDuration = "00:20")
        )

        coEvery { gameStatRepository.getAllGameStat() } returns flowOf(stats)


        viewModel.getAllGameStat()

        advanceUntilIdle()


        assertEquals(stats, viewModel.gameStats.value)
    }



    @Test
    fun testInitializeMiceCreatesMiceWithCorrectAttributes() {
        viewModel.initializeMice(3, 500f, 500f, 50f)

        val mice = viewModel.gameUiState.value.mice
        assertEquals(3, mice.size)

        mice.forEach { mouse ->
            assertTrue(mouse.x in 0f..450f)
            assertTrue(mouse.y in 0f..450f)
            assertEquals(50.dp, mouse.size)
        }
    }

    @Test
    fun testUpdateMicePositionsChangesMousePositions() {
        viewModel.initializeMice(2, 500f, 500f, 50f)
        val initialPositions = viewModel.gameUiState.value.mice.map { it.x to it.y }

        viewModel.updateMicePositions(500f, 500f, 50f)
        val updatedPositions = viewModel.gameUiState.value.mice.map { it.x to it.y }

        assertNotEquals(initialPositions, updatedPositions)
    }

}
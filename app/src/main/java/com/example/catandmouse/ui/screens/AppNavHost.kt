package com.example.catandmouse.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.catandmouse.ui.GameViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: GameViewModel,
    onStartGame: () -> Unit,
    onShowScore: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            StartScreen(
                viewModel = viewModel,
                onStartGame = onStartGame,
                onShowScore = onShowScore,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            )
        }
        composable(Screen.Game.route) {
            GameScreen(
                viewModel = viewModel,
                onStop = onStop,
                modifier = Modifier.fillMaxSize()

            )
        }
        composable(Screen.Stats.route) {
            StatsScreen(
                gameStats = viewModel.gameStats.collectAsState().value,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

sealed class Screen(val route: String) {
    data object Home : Screen(route = "start")
    data object Game : Screen(route = "game")
    data object Stats : Screen(route = "stats")
}
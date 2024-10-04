package com.example.catandmouse.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.catandmouse.ui.screens.AppNavHost
import com.example.catandmouse.ui.screens.Screen

@Composable
fun CatAndMouseApp(
    viewModel: GameViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {

            AppNavHost(
                navController = navController,
                viewModel = viewModel,
                onStartGame = {
                    viewModel.start()
                    navController.navigate(Screen.Game.route)
                },
                onShowScore = { navController.navigate(Screen.Stats.route)},
                onStop = {
                    navController.navigate(Screen.Home.route)
                    viewModel.stop()
                },
                modifier = Modifier.padding(it)
            )
        }
    }
}
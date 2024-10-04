package com.example.catandmouse.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catandmouse.data.GameStat


@Composable
fun StatsScreen(
    gameStats: List<GameStat>,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Статистика",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displayLarge,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(gameStats) { stat ->
                Card(
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .padding(4.dp)
                        .widthIn(max = 400.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Игра ${stat.id}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        HorizontalDivider()
                        Spacer(modifier = Modifier.padding(top = 4.dp))
                        Text(
                            text = "Всего нажатий: ${stat.totalTaps}",
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Попаданий: ${stat.successfulTaps}",
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Точность: ${stat.accuracy}%",
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Время: ${stat.gameDuration}",
                            fontSize = 16.sp

                        )
                    }
                }

            }

        }

    }

}
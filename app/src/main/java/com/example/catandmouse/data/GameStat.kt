package com.example.catandmouse.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_stats")
data class GameStat(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val totalTaps: Int,
    val successfulTaps: Int,
    val accuracy: Int,
    val gameDuration: String
)

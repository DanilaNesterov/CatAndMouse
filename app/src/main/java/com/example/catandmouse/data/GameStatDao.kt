package com.example.catandmouse.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface GameStatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGameStat(gameStat: GameStat)

    @Delete
    suspend fun removeGameStat(gameStat: GameStat)

    @Query("SELECT * FROM game_stats ORDER BY id DESC LIMIT 10")
    fun getLastTenGameStat(): Flow<List<GameStat>>

    @Query("SELECT COUNT(*) FROM game_stats")
    suspend fun getCount(): Int

    @Query("SELECT * FROM game_stats ORDER BY id ASC LIMIT 1")
    suspend fun getOldestGameStat(): GameStat?
}

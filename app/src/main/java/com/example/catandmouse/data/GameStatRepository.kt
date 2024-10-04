package com.example.catandmouse.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GameStatRepository {
    suspend fun addGameStat(gameStat: GameStat)

    suspend fun removeGameState(gameStat: GameStat)

    fun getAllGameStat(): Flow<List<GameStat>>

}

class GameStatRepositoryImpl @Inject constructor(
    private val gameStatDao: GameStatDao
) : GameStatRepository {
    override suspend fun addGameStat(gameStat: GameStat) {
        val count = gameStatDao.getCount()

        if (count >= 10) {
            val oldestGameStat = gameStatDao.getOldestGameStat()
            if (oldestGameStat != null) {
                gameStatDao.removeGameStat(oldestGameStat)
            }
        }
        gameStatDao.addGameStat(gameStat)
    }

    override suspend fun removeGameState(gameStat: GameStat) {
        return gameStatDao.removeGameStat(gameStat)
    }

    override fun getAllGameStat(): Flow<List<GameStat>> {
        return gameStatDao.getLastTenGameStat()
    }

}
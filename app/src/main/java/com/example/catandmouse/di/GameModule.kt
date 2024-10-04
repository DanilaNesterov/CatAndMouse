package com.example.catandmouse.di

import android.content.Context
import androidx.room.Room
import com.example.catandmouse.data.AppDatabase
import com.example.catandmouse.data.GameStatDao
import com.example.catandmouse.data.GameStatRepository
import com.example.catandmouse.data.GameStatRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "cat_and_mouse_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideGameStatDao(database: AppDatabase): GameStatDao {
        return database.gameStatDao()
    }


    @Provides
    @Singleton
    fun providesGameStatRepository(gameStatDao: GameStatDao): GameStatRepository {
        return GameStatRepositoryImpl(gameStatDao)
    }


}
package com.example.myplayer.di

import android.content.Context
import androidx.room.Room
import com.example.myplayer.data.local.MyPlayerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MyPlayerDatabase {
        return Room.databaseBuilder(
            context,
            MyPlayerDatabase::class.java,
            "my_player_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePlaylistDao(database: MyPlayerDatabase) = database.playlistDao()

    @Provides
    @Singleton
    fun provideHistoryDao(database: MyPlayerDatabase) = database.historyDao()
}
package com.example.myplayer.di

import com.example.myplayer.data.repository.HistoryRepositoryImpl
import com.example.myplayer.data.repository.MediaRepositoryImpl
import com.example.myplayer.data.repository.PlaylistRepositoryImpl
import com.example.myplayer.domain.repository.HistoryRepository
import com.example.myplayer.domain.repository.MediaRepository
import com.example.myplayer.domain.repository.PlaylistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMediaRepository(impl: MediaRepositoryImpl): MediaRepository

    @Binds
    @Singleton
    abstract fun bindPlaylistRepository(impl: PlaylistRepositoryImpl): PlaylistRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository
}
package com.example.myplayer.domain.repository

import com.example.myplayer.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun addToPlaylist(playlistId: Long, mediaId: Long)
    suspend fun removeFromPlaylist(playlistId: Long, mediaId: Long)
}
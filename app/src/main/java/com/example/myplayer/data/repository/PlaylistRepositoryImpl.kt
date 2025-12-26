package com.example.myplayer.data.repository

import com.example.myplayer.data.local.dao.PlaylistDao
import com.example.myplayer.data.local.entity.PlaylistEntity
import com.example.myplayer.domain.model.Playlist
import com.example.myplayer.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao
) : PlaylistRepository {

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getPlaylists().map { entities ->
            entities.map { it.toPlaylist() }
        }
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(playlist.toPlaylistEntity())
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(playlist.toPlaylistEntity())
    }

    override suspend fun addToPlaylist(playlistId: Long, mediaId: Long) {
        playlistDao.getPlaylist(playlistId)?.let { playlist ->
            val updatedMediaIds = playlist.mediaIds.toMutableList().apply { add(mediaId) }
            playlistDao.updatePlaylist(playlist.copy(mediaIds = updatedMediaIds))
        }
    }

    override suspend fun removeFromPlaylist(playlistId: Long, mediaId: Long) {
        playlistDao.getPlaylist(playlistId)?.let { playlist ->
            val updatedMediaIds = playlist.mediaIds.toMutableList().apply { remove(mediaId) }
            playlistDao.updatePlaylist(playlist.copy(mediaIds = updatedMediaIds))
        }
    }
}

private fun PlaylistEntity.toPlaylist(): Playlist {
    return Playlist(
        id = this.id,
        name = this.name,
        mediaIds = this.mediaIds
    )
}

private fun Playlist.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = this.id,
        name = this.name,
        mediaIds = this.mediaIds
    )
}
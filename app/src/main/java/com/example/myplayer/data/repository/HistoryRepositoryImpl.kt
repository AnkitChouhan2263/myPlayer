package com.example.myplayer.data.repository

import com.example.myplayer.data.local.dao.HistoryDao
import com.example.myplayer.data.local.entity.HistoryEntity
import com.example.myplayer.domain.model.History
import com.example.myplayer.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override fun getHistory(): Flow<List<History>> {
        return historyDao.getHistory().map { entities ->
            entities.map { it.toHistory() }
        }
    }

    override suspend fun addToHistory(history: History) {
        historyDao.insertHistory(history.toHistoryEntity())
    }

    override suspend fun clearHistory() {
        historyDao.clearHistory()
    }
}

private fun HistoryEntity.toHistory(): History {
    return History(
        id = this.id,
        mediaId = this.mediaId,
        timestamp = this.timestamp
    )
}

private fun History.toHistoryEntity(): HistoryEntity {
    return HistoryEntity(
        id = this.id,
        mediaId = this.mediaId,
        timestamp = this.timestamp
    )
}
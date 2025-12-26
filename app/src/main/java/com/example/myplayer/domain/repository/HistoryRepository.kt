package com.example.myplayer.domain.repository

import com.example.myplayer.domain.model.History
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistory(): Flow<List<History>>
    suspend fun addToHistory(history: History)
    suspend fun clearHistory()
}
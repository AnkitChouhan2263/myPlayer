package com.example.myplayer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myplayer.data.local.converter.ListConverter
import com.example.myplayer.data.local.dao.HistoryDao
import com.example.myplayer.data.local.dao.PlaylistDao
import com.example.myplayer.data.local.entity.HistoryEntity
import com.example.myplayer.data.local.entity.PlaylistEntity

@Database(
    entities = [PlaylistEntity::class, HistoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListConverter::class)
abstract class MyPlayerDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun historyDao(): HistoryDao
}
package com.example.myplayer.data.local.converter

import androidx.room.TypeConverter

class ListConverter {
    @TypeConverter
    fun fromString(value: String): List<Long> {
        return value.split(",").map { it.toLong() }
    }

    @TypeConverter
    fun fromList(list: List<Long>): String {
        return list.joinToString(",")
    }
}
package com.katharina.recipesapp.data.db

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? {
        if (dateString == null) {
            return null
        } else {
            return LocalDateTime.parse(dateString)
        }
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? {
        if (date == null) {
            return null
        } else {
            return date.toString()
        }
    }

    @TypeConverter
    fun fromStringList(value: String): List<String> {
        try {
            return Json.decodeFromString(value)
        } catch (e: Exception) {
            return emptyList()
        }
    }

    @TypeConverter
    fun toStringList(list: List<String>): String = Json.encodeToString(list)
}

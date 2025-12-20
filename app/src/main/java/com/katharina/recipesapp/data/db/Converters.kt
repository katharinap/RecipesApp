package com.katharina.recipesapp.data.db

import androidx.room.TypeConverter
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
    fun fromStringList(value: String): List<String> = value.split(",")

    @TypeConverter
    fun toStringList(list: List<String>): String = list.joinToString(",")
}

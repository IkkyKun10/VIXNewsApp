package com.riezki.vixnewsapp.data.local

import androidx.room.TypeConverter
import com.riezki.vixnewsapp.model.response.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name.toString()
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}
package ru.egoncharovsky.words.database.converters

import androidx.room.TypeConverter
import ru.egoncharovsky.words.domain.entity.Language

class LanguageConverter {

    @TypeConverter
    fun toLanguage(value: String) = enumValueOf<Language>(value)

    @TypeConverter
    fun fromHealth(value: Language) = value.name
}
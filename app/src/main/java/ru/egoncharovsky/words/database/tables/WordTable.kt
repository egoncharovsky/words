package ru.egoncharovsky.words.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.egoncharovsky.words.domain.entity.Language

@Entity
data class WordTable(
    @PrimaryKey val id: Long? = null,
    val value: String,
    val translation: String,
    val language: Language,
    val translationLanguage: Language
)





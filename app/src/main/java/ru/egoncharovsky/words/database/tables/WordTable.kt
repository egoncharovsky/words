package ru.egoncharovsky.words.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.egoncharovsky.words.domain.entity.Language
import ru.egoncharovsky.words.domain.entity.Word

@Entity
data class WordTable(
    @PrimaryKey val id: Long? = null,
    val value: String,
    val translation: String,
    val language: Language,
    val translationLanguage: Language
) {
    fun toEntity() = Word(
        id,
        value,
        translation,
        language,
        translationLanguage
    )

    companion object {
        fun fromEntity(word: Word) = WordTable(
            word.id,
            word.value,
            word.translation,
            word.language,
            word.translationLanguage
        )
    }
}





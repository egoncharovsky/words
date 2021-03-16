package ru.egoncharovsky.words.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.egoncharovsky.words.domain.entity.Language
import ru.egoncharovsky.words.domain.entity.Word

@Entity
data class WordTable(
    @ColumnInfo(name = "wordId")
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
        fun fromEntity(entity: Word) = WordTable(
            entity.id,
            entity.value,
            entity.translation,
            entity.language,
            entity.translationLanguage
        )
    }
}





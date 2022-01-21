package ru.egoncharovsky.words.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.egoncharovsky.words.database.Timestamps.fromMillis
import ru.egoncharovsky.words.database.Timestamps.toMillis
import ru.egoncharovsky.words.domain.entity.Language
import ru.egoncharovsky.words.domain.entity.Word
import java.time.LocalDateTime

@Entity
data class WordTable(
    @ColumnInfo(name = "wordId")
    @PrimaryKey val id: Long? = null,
    val createdAt: Long,

    val value: String,
    val translation: String,
    val language: Language,
    val translationLanguage: Language
) {

    fun toEntity() = Word(
        id,
        fromMillis(createdAt),
        value,
        translation,
        language,
        translationLanguage
    )

    companion object {
        fun fromEntity(entity: Word): WordTable = WordTable(
            entity.id,
            toMillis(entity.createdAt ?: LocalDateTime.now()),
            entity.value,
            entity.translation,
            entity.language,
            entity.translationLanguage
        )
    }
}





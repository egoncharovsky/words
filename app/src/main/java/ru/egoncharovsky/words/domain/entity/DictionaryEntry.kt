package ru.egoncharovsky.words.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class DictionaryEntry(
    @PrimaryKey override val id: Long?,
    @Relation(
        parentColumn = "wordId",
        entityColumn = "id"
    ) val word: Word
) : Identifiable<DictionaryEntry, Long> {
}

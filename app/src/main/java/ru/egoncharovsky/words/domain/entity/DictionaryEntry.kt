package ru.egoncharovsky.words.domain.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DictionaryEntry(
    @PrimaryKey override val id: Long?,
    @Embedded val word: Word
) : Identifiable<DictionaryEntry, Long> {
    override fun copy(newId: Long): DictionaryEntry = copy(id = newId)
}

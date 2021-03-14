package ru.egoncharovsky.words.database.tables

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import ru.egoncharovsky.words.domain.entity.DictionaryEntry

@Entity
data class DictionaryEntryTable(
    @PrimaryKey val id: Long?,
    val wordId: Long
) {
}

data class DictionaryEntryWord(
    @Embedded val dictionaryEntry: DictionaryEntryTable,
    @Relation(
        parentColumn = "wordId",
        entityColumn = "id"
    ) val word: WordTable
) {
    fun toEntity() = DictionaryEntry(
        dictionaryEntry.id,
        word.toEntity()
    )

    companion object {
        fun fromEntity(dictionaryEntry: DictionaryEntry) = DictionaryEntryWord(
            DictionaryEntryTable(
                dictionaryEntry.id,
                dictionaryEntry.word.id!!
            ),
            WordTable.fromEntity(dictionaryEntry.word)
        )
    }
}



package ru.egoncharovsky.words.database.tables

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class DictionaryEntryTable(
    @PrimaryKey val id: Long? = null,
    val wordId: Long
)

data class DictionaryEntryWordJoin(
    @Embedded val dictionaryEntry: DictionaryEntryTable,
    @Relation(
        parentColumn = "wordId",
        entityColumn = "id"
    ) val word: WordTable
)



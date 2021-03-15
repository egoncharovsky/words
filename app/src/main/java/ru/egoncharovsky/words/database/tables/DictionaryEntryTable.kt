package ru.egoncharovsky.words.database.tables

import androidx.room.*
import ru.egoncharovsky.words.domain.entity.DictionaryEntry

@Entity
data class DictionaryEntryTable(
    @ColumnInfo(name = "dictionaryEntryId")
    @PrimaryKey val id: Long? = null,
    val wordId: Long
)

data class DictionaryEntryWordJoin(
    @Embedded val dictionaryEntry: DictionaryEntryTable,
    @Relation(
        parentColumn = "wordId",
        entityColumn = "wordId"
    ) val word: WordTable
) {
    fun toEntity() = DictionaryEntry(
        dictionaryEntry.id,
        word.toEntity()
    )

}



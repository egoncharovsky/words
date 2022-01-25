package ru.egoncharovsky.words.database.tables

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = WordTable::class,
        parentColumns = ["wordId"],
        childColumns = ["wordId"],
        onDelete = CASCADE
    )]
)
data class WordPopularityTable(
    @PrimaryKey
    val wordId: Long,
    val rating: Int
)
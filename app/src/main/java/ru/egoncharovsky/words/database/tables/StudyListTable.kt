package ru.egoncharovsky.words.database.tables

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import ru.egoncharovsky.words.domain.entity.StudyList

@Entity
data class StudyListTable(
    @ColumnInfo(name = "studyListId")
    @PrimaryKey val id: Long? = null,
    val name: String,
) {
    companion object {
        fun fromEntity(studyList: StudyList) =
            StudyListTable(studyList.id, studyList.name)
    }
}

@Entity(primaryKeys = ["studyListId", "wordId"], indices = [Index("wordId")])
data class StudyListWordCrossRef(
    val studyListId: Long,
    val wordId: Long
) {
    companion object {
        fun toWordIds(studyList: StudyList): Set<Long> =
            studyList.words.map { it.id!! }.toSet()

        fun toCrossRefs(studyListId: Long, wordIds: Set<Long>) =
            wordIds.map { StudyListWordCrossRef(studyListId, it) }.toSet()
    }
}

data class StudyListWordJoin(
    @Embedded val studyList: StudyListTable,
    @Relation(
        parentColumn = "studyListId",
        entityColumn = "wordId",
        associateBy = Junction(StudyListWordCrossRef::class)
    ) val words: Set<WordTable>
) {
    fun toEntity() = StudyList(
        studyList.id,
        studyList.name,
        words.map { it.toEntity() }.toSet()
    )
}

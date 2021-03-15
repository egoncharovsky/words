package ru.egoncharovsky.words.database.tables

import androidx.room.*
import ru.egoncharovsky.words.domain.entity.StudyList

@Entity
data class StudyListTable(
    @ColumnInfo(name = "studyListId")
    @PrimaryKey val id: Long? = null,
    val name: String,
)

@Entity(primaryKeys = ["studyListId", "wordId"])
data class StudyListWordCrossRef(
    val studyListId: Long,
    val wordId: Long
)

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

    companion object {
        fun fromEntity(studyList: StudyList) = StudyListWordJoin(
            StudyListTable(studyList.id, studyList.name),
            studyList.words.map {
                WordTable(
                    it.id,
                    it.value,
                    it.translation,
                    it.language,
                    it.translationLanguage
                )
            }.toSet()
        )

    }
}

package ru.egoncharovsky.words.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.database.tables.StudyListTable
import ru.egoncharovsky.words.database.tables.StudyListWordCrossRef
import ru.egoncharovsky.words.database.tables.StudyListWordJoin
import ru.egoncharovsky.words.database.tables.WordTable

@Dao
abstract class StudyListDao {

    @Transaction
    @Query("SELECT * FROM StudyListTable")
    abstract fun getAll(): Flow<List<StudyListWordJoin>>

    @Transaction
    @Query("SELECT * FROM StudyListTable WHERE studyListId = :id")
    abstract fun get(id: Long): Flow<StudyListWordJoin>

    @Transaction
    open fun insert(entity: StudyListWordJoin): Long {
        val studyListId = insert(entity.studyList)
        val crossRefs = toCrossRefs(studyListId, entity.words)
        insertAll(crossRefs)
        return studyListId
    }

    @Insert
    abstract fun insert(entity: StudyListTable): Long

    @Insert
    abstract fun insertAll(entity: Collection<StudyListWordCrossRef>): List<Long>

    @Transaction
    open fun update(entity: StudyListWordJoin): Int {
        val studyListId = entity.studyList.id!!
        val updatedCount = update(entity.studyList)

        deleteAllStudyListWordCrossRef()
        val crossRefs = toCrossRefs(studyListId, entity.words)
        insertAll(crossRefs)
        return updatedCount
    }

    @Update
    abstract fun update(entity: StudyListTable): Int

    @Query("DELETE FROM StudyListWordCrossRef")
    abstract fun deleteAllStudyListWordCrossRef()

    private fun toCrossRefs(studyListId: Long, words: Set<WordTable>): Set<StudyListWordCrossRef> = words.map {
        StudyListWordCrossRef(
            studyListId,
            it.id!!
        )
    }.toSet()
}
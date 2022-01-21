package ru.egoncharovsky.words.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.database.tables.StudyListTable
import ru.egoncharovsky.words.database.tables.StudyListWordCrossRef
import ru.egoncharovsky.words.database.tables.StudyListWordCrossRef.Companion.toCrossRefs
import ru.egoncharovsky.words.database.tables.StudyListWordJoin

@Dao
abstract class StudyListDao {

    @Transaction
    @Query("SELECT * FROM StudyListTable")
    abstract fun getAll(): Flow<List<StudyListWordJoin>>

    @Transaction
    @Query("SELECT * FROM StudyListTable WHERE studyListId = :id")
    abstract fun get(id: Long): Flow<StudyListWordJoin>

    @Transaction
    open fun insert(entity: StudyListTable, wordIds: Set<Long>): Long {
        val studyListId = insert(entity)
        val crossRefs = toCrossRefs(studyListId, wordIds)
        insertAll(crossRefs)
        return studyListId
    }

    @Insert
    abstract fun insert(entity: StudyListTable): Long

    @Insert
    abstract fun insertAll(entity: Collection<StudyListWordCrossRef>): List<Long>

    @Transaction
    open fun update(entity: StudyListTable, wordIds: Set<Long>): Int {
        val studyListId = entity.id!!
        val updatedCount = update(entity)

        deleteStudyListWordCrossRefByStudyListId(studyListId)
        val crossRefs = toCrossRefs(studyListId, wordIds)
        insertAll(crossRefs)
        return updatedCount
    }

    @Update
    abstract fun update(entity: StudyListTable): Int

    @Query("DELETE FROM StudyListWordCrossRef WHERE studyListId = :studyListId")
    abstract fun deleteStudyListWordCrossRefByStudyListId(studyListId: Long)

    @Transaction
    open fun delete(id: Long) {
        deleteStudyListWordCrossRefByStudyListId(id)
        deleteById(id)
    }

    @Query("DELETE FROM StudyListTable WHERE studyListId = :id")
    abstract fun deleteById(id: Long)
}
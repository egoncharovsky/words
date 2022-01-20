package ru.egoncharovsky.words.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.database.tables.WordTable

@Dao
interface WordDao {

    @Query("SELECT * FROM WordTable")
    fun getAll(): Flow<List<WordTable>>

    @Query("SELECT * FROM WordTable WHERE value LIKE :pattern OR translation LIKE :pattern")
    fun searchWords(pattern: String): Flow<List<WordTable>>

    @Query("SELECT * FROM WordTable WHERE wordId IN (:ids)")
    fun get(ids: Set<Long>): Flow<List<WordTable>>

    @Query("SELECT * FROM WordTable WHERE value = :value AND translation = :translation")
    suspend fun find(value: String, translation: String): WordTable?

    @Insert
    suspend fun insert(entity: WordTable): Long

    @Query(
        """
            SELECT wt.* FROM WordTable wt
            LEFT JOIN StudyListWordCrossRef slw ON wt.wordId = slw.wordId 
            WHERE slw.studyListId IS NULL
        """
    )
    fun findNotIncludedInStudyLists(): Flow<List<WordTable>>

    @Query(
        """
            SELECT wt.* FROM WordTable wt 
            LEFT JOIN StudyListWordCrossRef slw ON wt.wordId = slw.wordId 
            WHERE slw.studyListId IS NULL
            AND wt.value LIKE :pattern OR wt.translation LIKE :pattern
        """
    )
    fun searchNotIncludedInStudyLists(pattern: String): Flow<List<WordTable>>

    @Query(
        """
            SELECT wt.* FROM WordTable wt
            WHERE wt.wordId IN (:ids)
            AND wt.value LIKE :pattern OR wt.translation LIKE :pattern
        """
    )
    fun searchInWordsWithIds(pattern: String, ids: Set<Long>): Flow<List<WordTable>>

    @Query(
        """
            SELECT wt.wordId FROM WordTable wt
            LEFT JOIN StudyListWordCrossRef slw ON wt.wordId = slw.wordId 
            WHERE slw.studyListId IS NOT NULL
        """
    )
    fun findWordsIdsIncludedInStudyLists(): Flow<List<Long>>

    @Query(
        """
            SELECT wt.wordId FROM WordTable wt
            LEFT JOIN StudyListWordCrossRef slw ON wt.wordId = slw.wordId 
            WHERE slw.studyListId IS NOT NULL
             AND slw.studyListId != :studyListId
        """
    )
    fun findWordsIdsIncludedInStudyListsExcluding(studyListId: Long): Flow<List<Long>>
}
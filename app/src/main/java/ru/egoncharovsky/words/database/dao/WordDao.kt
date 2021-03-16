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
}
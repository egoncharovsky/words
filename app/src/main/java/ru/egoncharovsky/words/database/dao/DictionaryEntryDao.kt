package ru.egoncharovsky.words.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.domain.entity.DictionaryEntry

@Dao
interface DictionaryEntryDao {

    @Query("SELECT * FROM DictionaryEntry")
    fun getAll(): Flow<List<DictionaryEntry>>

    @Query("SELECT * FROM DictionaryEntry WHERE id = :id")
    fun find(id: Long): Flow<DictionaryEntry?>

    @Update
    suspend fun update(entity: DictionaryEntry): Int

    @Insert
    suspend fun insert(entity: DictionaryEntry): Long

    @Insert
    suspend fun insertAll(entity: Collection<DictionaryEntry>): List<Long>

    @Delete
    suspend fun delete(entity: DictionaryEntry)

    @Query("DELETE FROM DictionaryEntry")
    suspend fun deleteAll()

    @Query("SELECT * FROM DictionaryEntry WHERE LOWER(value) LIKE LOWER(:pattern) OR LOWER(translation) LIKE LOWER(:pattern)")
    fun searchWord(pattern: String): Flow<List<DictionaryEntry>>
}
package ru.egoncharovsky.words.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.database.tables.DictionaryEntryWord

@Dao
interface DictionaryEntryDao {

    @Transaction
    @Query("SELECT * FROM DictionaryEntryTable")
    fun getAll(): Flow<List<DictionaryEntryWord>>

    @Transaction
    @Query("SELECT * FROM DictionaryEntryTable WHERE id = :id")
    fun find(id: Long): Flow<DictionaryEntryWord?>

    @Transaction
    @Update
    suspend fun update(entity: DictionaryEntryWord): Int

    @Transaction
    @Insert
    suspend fun insert(entity: DictionaryEntryWord): Long

    @Transaction
    @Insert
    suspend fun insertAll(entity: Collection<DictionaryEntryWord>): List<Long>

    @Transaction
    @Delete
    suspend fun delete(entity: DictionaryEntryWord)

    @Transaction
    @Query("DELETE FROM DictionaryEntryTable")
    suspend fun deleteAll()

//    @Transaction
//    @Query("""SELECT * FROM DictionaryEntry WHERE word = 1""")
//    fun searchWord(pattern: String): Flow<List<DictionaryEntry>>
}
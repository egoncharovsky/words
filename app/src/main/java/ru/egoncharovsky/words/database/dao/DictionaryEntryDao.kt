package ru.egoncharovsky.words.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.database.tables.DictionaryEntryTable
import ru.egoncharovsky.words.database.tables.DictionaryEntryWord

@Dao
interface DictionaryEntryDao {

    @Transaction
    @Query("SELECT * FROM DictionaryEntryTable")
    fun getAll(): Flow<List<DictionaryEntryWord>>

    @Transaction
    @Query("SELECT * FROM DictionaryEntryTable WHERE id = :id")
    fun find(id: Long): Flow<DictionaryEntryTable?>

    @Transaction
    @Update
    suspend fun update(entity: DictionaryEntryTable): Int

    @Transaction
    @Insert
    suspend fun insert(entity: DictionaryEntryTable): Long

    @Transaction
    @Insert
    suspend fun insertAll(entity: Collection<DictionaryEntryTable>): List<Long>

    @Transaction
    @Delete
    suspend fun delete(entity: DictionaryEntryTable)

    @Transaction
    @Query("DELETE FROM DictionaryEntryTable")
    suspend fun deleteAll()

//    @Transaction
//    @Query("""SELECT * FROM DictionaryEntry WHERE word = 1""")
//    fun searchWord(pattern: String): Flow<List<DictionaryEntry>>
}
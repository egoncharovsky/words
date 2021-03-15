package ru.egoncharovsky.words.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.database.tables.DictionaryEntryTable
import ru.egoncharovsky.words.database.tables.DictionaryEntryWordJoin

@Dao
interface DictionaryEntryDao {

    @Transaction
    @Query("SELECT * FROM DictionaryEntryTable")
    fun getAll(): Flow<List<DictionaryEntryWordJoin>>

    @Transaction
    @Query("SELECT * FROM DictionaryEntryTable WHERE id = :id")
    fun find(id: Long): Flow<DictionaryEntryWordJoin?>

    @Query("SELECT EXISTS(SELECT * FROM DictionaryEntryTable WHERE wordId = :wordId)")
    suspend fun isExist(wordId: Long): Boolean

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
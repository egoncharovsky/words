package ru.egoncharovsky.words.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.database.tables.WordPopularityTable

@Dao
interface WordPopularityDao {

    @Query("SELECT * FROM WordPopularityTable")
    fun getAll(): Flow<List<WordPopularityTable>>

    @Query("DELETE FROM WordPopularityTable")
    fun deleteAll()

    @Insert
    fun insertAll(entities: List<WordPopularityTable>): List<Long>
}
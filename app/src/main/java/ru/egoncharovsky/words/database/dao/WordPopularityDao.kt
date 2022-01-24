package ru.egoncharovsky.words.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.database.tables.WordPopularity

@Dao
interface WordPopularityDao {

    @Query("SELECT * FROM WordPopularity")
    fun getAll(): Flow<List<WordPopularity>>

    @Query("DELETE FROM WordPopularity")
    fun deleteAll()

    @Insert
    fun insertAll(entities: List<WordPopularity>): List<Long>
}
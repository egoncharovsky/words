package ru.egoncharovsky.words.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.egoncharovsky.words.database.tables.WordTable

@Dao
interface WordDao {

    @Query("SELECT * FROM WordTable WHERE value = :value AND translation = :translation")
    suspend fun find(value: String, translation: String): WordTable?

    @Insert
    suspend fun insert(entity: WordTable): Long
}
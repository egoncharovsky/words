package ru.egoncharovsky.words.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.egoncharovsky.words.database.converters.LanguageConverter
import ru.egoncharovsky.words.database.dao.DictionaryEntryDao
import ru.egoncharovsky.words.domain.entity.DictionaryEntry

@Database(entities = [DictionaryEntry::class], version = 1)
@TypeConverters(LanguageConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dictionaryEntryDao(): DictionaryEntryDao

    companion object {

        private const val NAME = "words-db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                NAME
            ).build()
        }
    }
}
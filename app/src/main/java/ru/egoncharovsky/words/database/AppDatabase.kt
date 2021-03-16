package ru.egoncharovsky.words.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.egoncharovsky.words.database.converters.LanguageConverter
import ru.egoncharovsky.words.database.dao.StudyListDao
import ru.egoncharovsky.words.database.dao.WordDao
import ru.egoncharovsky.words.database.tables.StudyListTable
import ru.egoncharovsky.words.database.tables.StudyListWordCrossRef
import ru.egoncharovsky.words.database.tables.WordTable

@Database(
    entities = [
        WordTable::class,
        StudyListTable::class,
        StudyListWordCrossRef::class
    ],
    version = 1
)
@TypeConverters(LanguageConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun studyListDao(): StudyListDao

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
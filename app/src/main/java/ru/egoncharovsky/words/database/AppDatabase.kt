package ru.egoncharovsky.words.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mu.KotlinLogging
import ru.egoncharovsky.words.database.converters.LanguageConverter
import ru.egoncharovsky.words.database.dao.StudyListDao
import ru.egoncharovsky.words.database.dao.WordDao
import ru.egoncharovsky.words.database.dao.WordPopularityDao
import ru.egoncharovsky.words.database.tables.StudyListTable
import ru.egoncharovsky.words.database.tables.StudyListWordCrossRef
import ru.egoncharovsky.words.database.tables.WordPopularityTable
import ru.egoncharovsky.words.database.tables.WordTable
import java.util.concurrent.Executors

@Database(
    entities = [
        WordTable::class,
        StudyListTable::class,
        StudyListWordCrossRef::class,
        WordPopularityTable::class
    ],
    version = 1
)
@TypeConverters(LanguageConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun studyListDao(): StudyListDao
    abstract fun wordPopularityDao(): WordPopularityDao

    companion object {

        private const val NAME = "words-db"

        private val logger = KotlinLogging.logger { }

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            val dbBuilder = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                NAME
            )
            dbBuilder.setQueryCallback({ sqlQuery, bindArgs ->
                logger.trace("SQL: $sqlQuery, Args: $bindArgs")
            }, Executors.newSingleThreadExecutor())

            return dbBuilder.build()
        }
    }
}
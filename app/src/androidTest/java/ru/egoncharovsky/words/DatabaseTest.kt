package ru.egoncharovsky.words

import androidx.room.Room
import org.junit.After
import org.junit.Before
import ru.egoncharovsky.words.database.AppDatabase
import java.io.IOException

abstract class DatabaseTest : AndroidTest() {

    lateinit var db: AppDatabase

    @Before
    open fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
    }

    @After
    @Throws(IOException::class)
    open fun closeDb() {
        db.close()
    }
}
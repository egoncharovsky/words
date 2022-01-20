package ru.egoncharovsky.words

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import ru.egoncharovsky.words.database.AppDatabase
import java.io.IOException

open class DatabaseTest {

    lateinit var db: AppDatabase

    @Before
    open fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
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
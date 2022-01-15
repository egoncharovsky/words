package ru.egoncharovsky.words.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
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
package ru.egoncharovsky.words

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import java.io.InputStream

abstract class AndroidTest {

    val context: Context = ApplicationProvider.getApplicationContext()
    val instrContext: Context = InstrumentationRegistry.getInstrumentation().context

    fun getResource(name: String): InputStream {
        val id = instrContext.resources.getIdentifier(
            name,
            "raw",
            instrContext.packageName
        )
        return instrContext.resources.openRawResource(id)
    }
}
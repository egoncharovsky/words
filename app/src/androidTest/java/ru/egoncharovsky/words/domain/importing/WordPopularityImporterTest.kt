package ru.egoncharovsky.words.domain.importing

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.egoncharovsky.words.DatabaseTest
import ru.egoncharovsky.words.database.tables.WordTable
import ru.egoncharovsky.words.domain.entity.Language
import ru.egoncharovsky.words.repository.persistent.WordRepository
import ru.egoncharovsky.words.repository.persistent.room.WordRepositoryRoom

@RunWith(AndroidJUnit4::class)
internal class WordPopularityImporterTest : DatabaseTest() {

    private lateinit var worRepository: WordRepository


    @Before
    fun setUp() {
        worRepository = WordRepositoryRoom(db.wordDao(), db.wordPopularityDao(), db)
    }

    @Test
    fun upgradePopularity() {
        val dictionary = getResource("words_statistic")

        val words = listOf(
            WordTable(1, 0L, "the", "перевод", Language.EN, Language.RU),
            WordTable(2, 0L, "it", "перевод2", Language.EN, Language.RU),
            WordTable(3, 0L, "word", "перевод3", Language.EN, Language.RU),
        )

        runBlocking {
            words.forEach { db.wordDao().insert(it) }
        }

        val importer = WordPopularityImporter(worRepository)

        runBlocking {
            importer.upgradePopularity(dictionary)

            val ratings = worRepository.getPopularityRatings().take(1).single()
            assertEquals(mapOf(1L to 2013540168, 2L to 1315882734), ratings)
        }
    }
}
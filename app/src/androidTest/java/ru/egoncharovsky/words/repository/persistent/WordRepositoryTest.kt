package ru.egoncharovsky.words.repository.persistent

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.egoncharovsky.words.DatabaseTest
import ru.egoncharovsky.words.database.tables.StudyListTable
import ru.egoncharovsky.words.database.tables.StudyListWordCrossRef
import ru.egoncharovsky.words.database.tables.WordTable
import ru.egoncharovsky.words.domain.entity.Language
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.repository.persistent.room.WordRepositoryRoom
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
internal class WordRepositoryTest : DatabaseTest() {

    lateinit var wordRepository: WordRepository

    @Before
    fun setUp() {
        wordRepository = WordRepositoryRoom(db.wordDao(), db.wordPopularityDao(), db)
    }

    @Test
    fun testFindWordsIdsIncludedInStudyLists() {
        val words = listOf(
            WordTable(1, 0L, "word", "перевод", Language.EN, Language.RU),
            WordTable(2, 0L, "word2", "перевод2", Language.EN, Language.RU),
            WordTable(3, 0L, "word3", "перевод3", Language.EN, Language.RU),
            WordTable(4, 0L, "word4", "перевод4", Language.EN, Language.RU)
        )

        runBlocking {
            words.forEach { db.wordDao().insert(it) }

            db.studyListDao().let {
                it.insert(StudyListTable(1, "List1"))
                it.insert(StudyListTable(2, "List2"))
                it.insert(StudyListTable(3, "List3"))
                it.insertAll(
                    listOf(
                        StudyListWordCrossRef(1, 1),
                        StudyListWordCrossRef(1, 2),
                        StudyListWordCrossRef(2, 1),
                        StudyListWordCrossRef(3, 3),
                    )
                )
            }
        }

        runBlocking {
            assertEquals(
                setOf<Long>(1, 2, 3),
                wordRepository.findWordsIdsIncludedInStudyListsExcluding().take(1).single()
            )
            assertEquals(
                setOf<Long>(1, 3),
                wordRepository.findWordsIdsIncludedInStudyListsExcluding(1).take(1).single()
            )
            assertEquals(
                setOf<Long>(1, 2, 3),
                wordRepository.findWordsIdsIncludedInStudyListsExcluding(2).take(1).single()
            )
            assertEquals(
                setOf<Long>(1, 2),
                wordRepository.findWordsIdsIncludedInStudyListsExcluding(3).take(1).single()
            )
        }
    }

    @Test
    fun testSaveImportedWords() {
        val words1 = listOf(
            Word("word1", "перевод1", Language.RU, Language.EN),
            Word("word2", "перевод2", Language.RU, Language.EN)
        )

        runBlocking {
            val savedIds = wordRepository.saveImportedWords(words1)

            assertEquals(listOf(1L, 2L), savedIds)
            val saved = wordRepository.getAll().take(1).single()
            assertEquals(2, saved.size)
            assertSaved(words1[0], 1L, saved[0])
            assertSaved(words1[1], 2L, saved[1])
        }

        val words2 = listOf(
            Word("word2", "перевод2", Language.RU, Language.EN),
            Word("word3", "перевод3", Language.RU, Language.EN)
        )

        runBlocking {
            val savedIds = wordRepository.saveImportedWords(words2)
            assertEquals(listOf(3L), savedIds)

            val saved = wordRepository.getAll().take(1).single()
            assertEquals(3, saved.size)
            assertSaved(words1[0], 1L, saved[0])
            assertSaved(words1[1], 2L, saved[1])
            assertSaved(words2[1], 3L, saved[2])
        }
    }

    @Test
    fun testSaveCreatedAtDate() {
        val dateTime = LocalDateTime.of(2022, 1, 20, 16, 32, 4)

        runBlocking {
            wordRepository.saveImportedWords(
                listOf(
                    Word(null, dateTime, "word1", "перевод1", Language.RU, Language.EN)
                )
            )

            val saved = wordRepository.getAll().take(1).single()[0]
            assertEquals(dateTime, saved.createdAt)
        }
    }

    @Test
    fun testUpgradePopularityRatings() {
        runBlocking {
            db.wordDao().insert(WordTable(1, 0L, "word", "перевод", Language.EN, Language.RU))
        }

        val ratings = mapOf(1L to 100)

        runBlocking {
            wordRepository.upgradePopularityRatings(ratings)

            val saved = wordRepository.getPopularityRatings().take(1).single()
            assertEquals(ratings, saved)
        }
    }

    private fun assertSaved(expected: Word, expectedId: Long, actual: Word) {
        assertEquals(expectedId, actual.id)
        assertNotNull(actual.createdAt)

        assertEquals(expected.value, actual.value)
        assertEquals(expected.translation, actual.translation)
        assertEquals(expected.language, actual.language)
        assertEquals(expected.translationLanguage, actual.translationLanguage)
    }
}
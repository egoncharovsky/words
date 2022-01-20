package ru.egoncharovsky.words.database.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.egoncharovsky.words.DatabaseTest
import ru.egoncharovsky.words.database.tables.StudyListTable
import ru.egoncharovsky.words.database.tables.StudyListWordCrossRef
import ru.egoncharovsky.words.database.tables.WordTable
import ru.egoncharovsky.words.domain.entity.Language

@RunWith(AndroidJUnit4::class)
internal class WordDaoTest : DatabaseTest() {
    private lateinit var wordDao: WordDao

    private val words = listOf(
        WordTable(1, 0L, "word", "перевод", Language.EN, Language.RU),
        WordTable(2, 0L, "word2", "перевод2", Language.EN, Language.RU),
        WordTable(3, 0L, "word3", "перевод3", Language.EN, Language.RU),
        WordTable(4, 0L, "word4", "перевод4", Language.EN, Language.RU),
        WordTable(5, 0L, "another", "другой", Language.EN, Language.RU),
    )

    @Before
    fun setUp() {
        wordDao = db.wordDao()

        runBlocking {
            words.forEach { wordDao.insert(it) }

            val studyListDao = db.studyListDao()
            studyListDao.insert(StudyListTable(1, "List1"))
            studyListDao.insert(StudyListTable(2, "List2"))

            studyListDao.insertAll(
                listOf(
                    StudyListWordCrossRef(1, 1),
                    StudyListWordCrossRef(1, 2),

                    StudyListWordCrossRef(2, 1),
                )
            )
        }
    }

    @Test(timeout = 1000)
    fun testGet() = runBlocking {
        val actual = wordDao.get(setOf(1, 2)).take(1).single()

        assertEquals(listOf(words[0], words[1]), actual)
    }

    @Test(timeout = 1000)
    fun testFindNotIncludedInStudyLists() = runBlocking {
        val actual = wordDao.findNotIncludedInStudyLists().take(1).single()

        assertEquals(listOf(words[2], words[3], words[4]), actual)
    }

    @Test(timeout = 1000)
    fun testSearchNotIncludedInStudyLists() = runBlocking {
        val actual = wordDao.searchNotIncludedInStudyLists("%word%").take(1).single()

        assertEquals(listOf(words[2], words[3]), actual)
    }

    @Test(timeout = 1000)
    fun testSearchInWordsWithIds() = runBlocking {
        val actual = wordDao.searchInWordsWithIds("%word%", setOf(2, 3, 4, 5)).take(1).single()

        assertEquals(listOf(words[1], words[2], words[3]), actual)
    }

}
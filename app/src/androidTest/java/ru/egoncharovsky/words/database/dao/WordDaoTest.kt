package ru.egoncharovsky.words.database.dao

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.egoncharovsky.words.database.DatabaseTest
import ru.egoncharovsky.words.database.tables.StudyListTable
import ru.egoncharovsky.words.database.tables.StudyListWordCrossRef
import ru.egoncharovsky.words.database.tables.WordTable
import ru.egoncharovsky.words.domain.entity.Language

internal class WordDaoTest : DatabaseTest() {
    private lateinit var wordDao: WordDao

    @Before
    fun setUp() {
        wordDao = db.wordDao()

        runBlocking {
            wordDao.insert(WordTable(1, "word", "перевод", Language.EN, Language.RU))
            wordDao.insert(WordTable(2, "word2", "перевод2", Language.EN, Language.RU))
            wordDao.insert(WordTable(3, "word3", "перевод3", Language.EN, Language.RU))

            val studyListDao = db.studyListDao()
            studyListDao.insert(StudyListTable(1, "List1"))

            studyListDao.insertAll(listOf(StudyListWordCrossRef(1, 1)))
        }
    }

    @Test
    fun findNotIncludedInStudyLists() = runBlocking {
        val actual = wordDao.findNotIncludedInStudyLists()

        assertEquals(
            actual,
            listOf(
                WordTable(2, "word2", "перевод2", Language.EN, Language.RU),
                WordTable(3, "word3", "перевод3", Language.EN, Language.RU)
            )
        )
    }
}
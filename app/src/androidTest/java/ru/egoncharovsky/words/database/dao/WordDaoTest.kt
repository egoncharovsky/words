package ru.egoncharovsky.words.database.dao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

    private val words = listOf(
        WordTable(1, "word", "перевод", Language.EN, Language.RU),
        WordTable(2, "word2", "перевод2", Language.EN, Language.RU),
        WordTable(3, "word3", "перевод3", Language.EN, Language.RU)
    )

    @Before
    fun setUp() {
        wordDao = db.wordDao()

        runBlocking {
            words.forEach { wordDao.insert(it) }

            val studyListDao = db.studyListDao()
            studyListDao.insert(StudyListTable(1, "List1"))

            studyListDao.insertAll(listOf(
                StudyListWordCrossRef(1, 1),
                StudyListWordCrossRef(1, 2)
            ))
        }
    }

    @Test(timeout = 1000)
    fun testGet() {
        GlobalScope.launch(Dispatchers.IO) {
            wordDao.get(setOf(1, 2)).collect {
                assertEquals(it, listOf(words[1], words[2]))
            }
        }
    }

    @Test(timeout = 1000)
    fun testFindNotIncludedInStudyLists() {
        GlobalScope.launch(Dispatchers.IO) {
            wordDao.findNotIncludedInStudyListsOrWithIds(setOf(2)).collect {
                assertEquals(it, listOf(words[2], words[3]))
            }
        }
    }
}
package ru.egoncharovsky.words.repository.persistent

import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.egoncharovsky.words.DatabaseTest
import ru.egoncharovsky.words.database.tables.StudyListTable
import ru.egoncharovsky.words.database.tables.StudyListWordCrossRef
import ru.egoncharovsky.words.database.tables.WordTable
import ru.egoncharovsky.words.domain.entity.Language
import ru.egoncharovsky.words.repository.persistent.room.WordRepositoryRoom

internal class WordRepositoryTest : DatabaseTest() {

    lateinit var wordRepository: WordRepository

    private val words = listOf(
        WordTable(1, "word", "перевод", Language.EN, Language.RU),
        WordTable(2, "word2", "перевод2", Language.EN, Language.RU),
        WordTable(3, "word3", "перевод3", Language.EN, Language.RU),
        WordTable(4, "word4", "перевод4", Language.EN, Language.RU)
    )

    @Before
    fun setUp() {
        val wordDao = db.wordDao()

        runBlocking {
            words.forEach { wordDao.insert(it) }

            val studyListDao = db.studyListDao()
            studyListDao.insert(StudyListTable(1, "List1"))
            studyListDao.insert(StudyListTable(2, "List2"))
            studyListDao.insert(StudyListTable(3, "List3"))

            studyListDao.insertAll(
                listOf(
                    StudyListWordCrossRef(1, 1),
                    StudyListWordCrossRef(1, 2),
                    StudyListWordCrossRef(2, 1),
                    StudyListWordCrossRef(3, 3),
                )
            )
        }

        wordRepository = WordRepositoryRoom(wordDao, db)
    }

    @Test
    fun testFindWordsIdsIncludedInStudyLists(): Unit = runBlocking {
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
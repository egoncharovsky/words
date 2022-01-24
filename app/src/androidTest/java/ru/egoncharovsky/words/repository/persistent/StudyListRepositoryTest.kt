package ru.egoncharovsky.words.repository.persistent

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.egoncharovsky.words.DatabaseTest
import ru.egoncharovsky.words.domain.entity.Language
import ru.egoncharovsky.words.domain.entity.StudyList
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.repository.persistent.room.StudyListRepositoryRoom
import ru.egoncharovsky.words.repository.persistent.room.WordRepositoryRoom
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
internal class StudyListRepositoryTest : DatabaseTest() {

    private lateinit var studyListRepository: StudyListRepository

    private val dateTime = LocalDateTime.of(2022, 1, 1, 15, 30)
    private val words = listOf(
        Word(1, dateTime, "word1", "перевод1", Language.EN, Language.RU),
        Word(2, dateTime, "word2", "перевод2", Language.EN, Language.RU),
        Word(3, dateTime, "word3", "перевод3", Language.EN, Language.RU),
    )

    @Before
    fun setUp() {
        val studyListDao = db.studyListDao()
        val wordRepository = WordRepositoryRoom(db.wordDao(), db.wordPopularityDao(), db)

        runBlocking {
            wordRepository.saveImportedWords(words)
        }

        studyListRepository = StudyListRepositoryRoom(studyListDao)
    }

    @Test
    fun testSave() = runBlocking {
        val studyLists = listOf(
            StudyList(null, "List1", setOf(words[0], words[1])),
            StudyList(null, "List2", setOf(words[1]))
        )
        studyLists.forEach(studyListRepository::save)

        val saved = studyListRepository.getAll().take(1).single()
        assertEquals(2, saved.size)
        assertSaved(studyLists[0], 1L, saved[0])
        assertSaved(studyLists[1], 2L, saved[1])
    }

    @Test
    fun testUpdate() = runBlocking {
        studyListRepository.save(StudyList(null, "List1", setOf(words[0], words[1])))

        val saved = studyListRepository.getAll().take(1).single()[0]

        val modified = StudyList(saved.id, "List2", setOf(words[1]))
        studyListRepository.save(modified)

        val updated = studyListRepository.getAll().take(1).single()
        assertEquals(1, updated.size)
        assertSaved(modified, 1L, updated[0])
    }

    private fun assertSaved(expected: StudyList, expectedId: Long, actual: StudyList) {
        assertEquals(expectedId, actual.id)

        assertEquals(expected.name, actual.name)
        assertEquals(expected.words, actual.words)
    }
}
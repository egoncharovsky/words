package ru.egoncharovsky.words.domain.service

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import ru.egoncharovsky.words.domain.entity.Language
import ru.egoncharovsky.words.domain.entity.Word

internal class WordServiceTest {

    private val words = listOf(
        Word(1, null, "word", "слово", Language.EN, Language.RU),
        Word(2, null, "word2", "слово2", Language.EN, Language.RU)
    )

    @Test
    fun test_findHidingAlreadyIncluded() = runBlocking {
        val service = WordService(mock {
            on { findNotIncludedInStudyLists() } doReturn flowOf(listOf(words[0]))
            on { get(setOf(2)) } doReturn flowOf(listOf(words[1]))
        })

        val actual = service.findHidingAlreadyIncluded(setOf(2)).take(1).single()

        assertEquals(actual, listOf(words[0], words[1]))
    }

    @Test
    fun test_searchHidingAlreadyIncluded() = runBlocking {
        val service = WordService(mock {
            on { searchNotIncludedInStudyLists("word") } doReturn flowOf(listOf(words[0]))
            on { searchInWordsWithIds("word", setOf(2)) } doReturn flowOf(listOf(words[1]))
        })

        val actual = service.searchHidingAlreadyIncluded("word", setOf(2)).take(1).single()

        assertEquals(actual, listOf(words[0], words[1]))
    }

    @Test
    fun testUnique_findHidingAlreadyIncluded() = runBlocking {
        val service = WordService(mock {
            on { findNotIncludedInStudyLists() } doReturn flowOf(listOf(words[0]))
            on { get(setOf(1)) } doReturn flowOf(listOf(words[0]))
        })

        val actual = service.findHidingAlreadyIncluded(setOf(1)).take(1).single()

        assertEquals(actual, listOf(words[0]))
    }

    @Test
    fun testUnique_searchHidingAlreadyIncluded() = runBlocking {
        val service = WordService(mock {
            on { searchNotIncludedInStudyLists("word") } doReturn flowOf(listOf(words[0]))
            on { searchInWordsWithIds("word", setOf(1)) } doReturn flowOf(listOf(words[0]))
        })

        val actual = service.searchHidingAlreadyIncluded("word", setOf(1)).take(1).single()

        assertEquals(actual, listOf(words[0]))
    }
}
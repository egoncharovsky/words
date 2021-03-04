package ru.egoncharovsky.words.domain.quiz

import org.junit.jupiter.api.Test
import ru.egoncharovsky.words.domain.Word
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class WordShufflerTest {

    @Test
    fun `Word should be taken exactly as progress limit`() {
        val word = QuizTest.dictionary[1]
        val shuffler = WordShuffler(setOf(word), 1, 3)

        assertEquals(listOf(word, word, word), shuffler.asSequence().toList())
    }

    @Test
    fun `All words should be taken`() {
        val words = QuizTest.dictionary.take(3)
        val progressLimit = 1
        val shuffler = WordShuffler(words.toSet(), 1, progressLimit)

        val shuffled = shuffler.asSequence().toList()
        assertTrue(shuffled.containsAll(words))
    }

    @Test
    fun `Each word should be taken exactly as progress limit`() {
        val words = QuizTest.dictionary.take(3)
        val progressLimit = 3
        val shuffler = WordShuffler(words.toSet(), 1, progressLimit)

        val shuffled = shuffler.asSequence().toList()

        val counts = shuffled.groupingBy { it }.eachCount()
        words.forEach { assertEquals(progressLimit, counts[it]) }
    }

    @Test
    fun `Words should not be repeated according to min distance during window stay full`() {
        val words = QuizTest.dictionary.take(20)
        val windowSize = 7
        val minDistance = 3

        val shuffler = WordShuffler(words.toSet(), windowSize, 3, minDistance)

        val shuffled = shuffler.asSequence().toList()
        println(shuffled.joinToString { it.value })
        val forChecking = shuffled.dropLast(windowSize)
        println(forChecking.joinToString { it.value })

        forChecking.windowed(size = windowSize, step = 1).forEach {
            for (distance in 1..minDistance) {
                val word = it[0]
                val neighbour = it[distance]
                assertNotEquals(word, neighbour, "$word has neighbour $neighbour in distance $distance")
            }
        }
    }

    @Test
    fun `On incorrect answer word should be returned again`() {
        val word = QuizTest.dictionary[1]
        val shuffler = WordShuffler(setOf(word), 1, 3)

        shuffler.next()
        shuffler.next()
        shuffler.decrementProgress(word)
        shuffler.next()

        assertTrue(shuffler.hasNext())
        assertEquals(word, shuffler.next())
        assertFalse(shuffler.hasNext())
    }

    @Test
    fun `Only words with incorrect answers should be returned again`() {
        val words = QuizTest.dictionary.take(3)
        val progressLimit = 3
        val shuffler = WordShuffler(words.toSet(), 1, progressLimit)

        val incorrectAnswers = 2
        var answered = 0

        val shuffled = mutableListOf<Word>()

        while (shuffler.hasNext()) {
            val word = shuffler.next()
            if (word == words[1] && answered < incorrectAnswers) {
                shuffler.decrementProgress(word)
                answered++
            }
            shuffled.add(word)
        }
        println(shuffled.joinToString { it.value })

        val counts = shuffled.groupingBy { it }.eachCount()
        assertEquals(progressLimit, counts[words[0]], "for word ${words[0]}")
        assertEquals(progressLimit + incorrectAnswers, counts[words[1]], "for word ${words[1]}")
        assertEquals(progressLimit, counts[words[2]], "for word ${words[2]}")
    }

    @Test
    fun totalProgressPercentage() {
        val words = QuizTest.dictionary.take(3)
        val shuffler = WordShuffler(words.toSet(), 1, 3)

        shuffler.next()
        assertEquals(11, shuffler.totalProgressPercentage())
        shuffler.next()
        assertEquals(22, shuffler.totalProgressPercentage())

        shuffler.asSequence().toList()
        assertEquals(100, shuffler.totalProgressPercentage())
    }
}
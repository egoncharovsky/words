package ru.egoncharovsky.words.domain.quiz

import org.junit.jupiter.api.Test
import ru.egoncharovsky.words.domain.Word
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class WordShufflerTest {

    private val dictionary = listOf(
        Word("apple", "яблоко"),
        Word("any", "любой"),
        Word("you", "ты"),
        Word("I", "я"),
        Word("many", "много"),
        Word("love", "любить"),
        Word("TV", "телевизор"),
        Word("shift", "сдвиг"),
        Word("weather", "погода"),
        Word("translation", "перевод"),
        Word("nice", "приятный"),
        Word("very", "очень"),
        Word("Greece", "Греция"),
        Word("cool", "круто"),
        Word("cold", "холодно"),
        Word("hot", "горячо"),
        Word("hungry", "голодный"),
        Word("breakfast", "завтра"),
    )

    @Test
    fun `Word should be taken exactly as progress limit`() {
        val word = dictionary[1]
        val shuffler = WordShuffler(listOf(word), 1, 3)

        assertEquals(listOf(word, word, word), shuffler.asSequence().toList())
    }

    @Test
    fun `All words should be taken`() {
        val words = dictionary.take(3)
        val progressLimit = 1
        val shuffler = WordShuffler(words, 1, progressLimit)

        val shuffled = shuffler.asSequence().toList()
        assertTrue(shuffled.containsAll(words))
    }

    @Test
    fun `Each word should be taken exactly as progress limit`() {
        val words = dictionary.take(3)
        val progressLimit = 3
        val shuffler = WordShuffler(words, 1, progressLimit)

        val shuffled = shuffler.asSequence().toList()

        val counts = shuffled.groupingBy { it }.eachCount()
        words.forEach { assertEquals(progressLimit, counts[it]) }
    }

    @Test
    fun `Words should not be repeated according to min distance during window stay full`() {
        val words = dictionary.take(20)
        val windowSize = 7
        val minDistance = 3

        val shuffler = WordShuffler(words, windowSize, 3, minDistance)

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
}
package ru.egoncharovsky.words.domain.quiz

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.egoncharovsky.words.domain.quiz.card.Meaning
import kotlin.reflect.cast

internal class QuizManagerTest {

    @Test
    fun next() {
        val words = QuizTest.dictionary.take(5)
        val manager = QuizManager(words.toSet())

        val progressLimit = manager.progressLimit

//        val cards = manager.asSequence().toList()
//
//        assertEquals(words.size*progressLimit, cards.size)
//
//        val meanings = cards.filter { it::class == Meaning::class }.map { (it as Meaning) }
//        words.forEach { word ->
//            assertTrue(meanings.map { it.word }.contains(word))
//        }


//        words.forEach { word ->
//            assertEquals(Meaning::class, cards.first { it.word == word }::class)
//        }

    }

    @Test
    fun answerCheckResult() {
    }
}
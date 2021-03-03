package ru.egoncharovsky.words.domain.quiz

import org.junit.jupiter.api.Test
import ru.egoncharovsky.words.domain.Word
import ru.egoncharovsky.words.domain.quiz.card.Card
import ru.egoncharovsky.words.domain.quiz.card.Meaning
import ru.egoncharovsky.words.domain.quiz.card.MultiChoice
import ru.egoncharovsky.words.domain.quiz.card.Question
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class QuizManagerTest {

    @Test
    fun `All words should be showed`() {
        val words = QuizTest.dictionary.take(5)
        val manager = QuizManager(words.toSet())

        val progressLimit = manager.progressLimit

        val cards = manager.takeAllWithCorrectAnswers()

        assertEquals(words.size * progressLimit, cards.size)

        val counts = cards.groupingBy { it.word }.eachCount()
        words.forEach {
            assertEquals(progressLimit, counts[it])
        }
    }

    @Test
    fun `Cards should start from meaning`() {
        val words = QuizTest.dictionary.take(5)
        val manager = QuizManager(words.toSet())

        val cards = manager.takeAllWithCorrectAnswers()

        words.forEach { word ->
            assertEquals(Meaning::class, cards.first { it.word == word }::class)
        }
    }

    @Test
    fun `Questions should contains only words with showed meanings`() {
        val words = QuizTest.dictionary.take(5)
        val manager = QuizManager(words.toSet())

        val cards = manager.takeAllWithCorrectAnswers()

        val showed = mutableSetOf<Word>()
        cards.forEach { card ->
            when (card) {
                is Meaning -> {
                    showed.add(card.word)
                }
                is MultiChoice -> {
                    assertTrue(showed.contains(card.word))
                    assertTrue(showed.map { it.translation }.containsAll(card.options))
                }
                is Question<*> -> {
                    assertTrue(showed.contains(card.word))
                }
            }
        }
    }

    @Test
    fun `Multi choice should not repeat options`() {
        val words = QuizTest.dictionary.take(5)
        val manager = QuizManager(words.toSet())

        val cards = manager.takeAllWithCorrectAnswers()

        cards.filterIsInstance<MultiChoice>().forEach {
            assertEquals(it.options.distinct(), it.options)
        }
    }

    @Test
    fun `Meaning should be showed after incorrect answer and then answered one more time`() {
        val words = QuizTest.dictionary.take(5)
        val manager = QuizManager(words.toSet())

        val progressLimit = manager.progressLimit

        val cards = mutableListOf<Card>()
        var card = manager.start()
        cards.add(card)
        while (manager.hasNext() && card !is Question<*>) {
            card = manager.next(card)
            cards.add(card)
        }

        val incorrectAnsweredWord = card.word
        val incorrectAnswerMock = QuestionWithIncorrectAnswer(incorrectAnsweredWord)

        val meaning = manager.next(incorrectAnswerMock, Any())

        assertEquals(Meaning::class, meaning::class)
        assertEquals(incorrectAnsweredWord, meaning.word)

        val allCards = cards.plus(meaning).plus(manager.takeAllWithCorrectAnswers())

        val wordCards = allCards.filter { it.word == incorrectAnsweredWord }
        val questionCount = wordCards.filterIsInstance<Question<*>>().count()

        // progress limit = meaning (1) + questions (5) = 6, here is 5 + 1 = 6 questions
        assertEquals(progressLimit, questionCount, """
            |wordCards: $wordCards
            |cards:     ${cards.joinToString { it::class.simpleName + ":" + it.word.value}}
            |allCards:  ${allCards.joinToString { it::class.simpleName + ":" + it.word.value}}
        """.trimMargin())
    }

    @Test
    fun answerCheckResult() {
    }

    private fun QuizManager.takeAllWithCorrectAnswers(): List<Card> {
        val cards = mutableListOf<Card>()

        var card = start()
        cards.add(card)
        while (hasNext()) {
            card = next(card)
            cards.add(card)
        }
        return cards
    }

    private fun QuizManager.next(card: Card): Card = when (card) {
        is Question<*> -> {
            @Suppress("UNCHECKED_CAST")
            val question: Question<Any> = card as Question<Any>
            next(question, question.correctAnswer())
        }
        is Meaning -> {
            next(card)
        }
        else -> throw IllegalStateException("Unknown card type ${card::class}")
    }

    class QuestionWithIncorrectAnswer(
        override val word: Word
    ) : Question<Any> {

        override fun checkAnswer(value: Any): Boolean = false

        override fun correctAnswer(): Any = Any()

    }

}
package ru.egoncharovsky.words.domain.quiz

import org.junit.jupiter.api.Test
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.domain.quiz.card.*
import java.util.function.Predicate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class QuizManagerTest {

    @Test
    fun `All words should be showed`() {
        val words = QuizTest.dictionary.take(5)
        val manager = QuizManager(words.toSet())

        val progressLimit = manager.progressLimit

        val cards = manager.takeAllWithCorrectAnswers()

        // 2 on cards on remember task
        assertEquals(words.size * (progressLimit + 1), cards.size)

        val counts = cards.groupingBy { it.word }.eachCount()
        words.forEach {
            assertEquals(progressLimit + 1, counts[it])
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

        val cards = manager.takeAllUpToPredicate { it is Question<*> }

        val incorrectAnswerMock = QuestionWithIncorrectAnswer(cards.last())
        val incorrectAnsweredWord = incorrectAnswerMock.word

        val meaning = manager.next(incorrectAnswerMock, Any())

        assertEquals(Meaning::class, meaning!!::class)
        assertEquals(incorrectAnsweredWord, meaning.word)

        val allCards = cards.plus(meaning).plus(manager.takeAllWithCorrectAnswers())

        val wordCards = allCards.filter { it.word == incorrectAnsweredWord }
        val questionCount = wordCards.filterIsInstance<Question<*>>().count()

        // progress limit = meaning (1) + questions (5) + right (1) = 7, here is 6 + 1 = 7 questions
        assertEquals(progressLimit + 1, questionCount, """
            |wordCards: $wordCards
            |cards:     ${cards.joinToString { it::class.simpleName + ":" + it.word.value}}
            |allCards:  ${allCards.joinToString { it::class.simpleName + ":" + it.word.value}}
        """.trimMargin())
    }

    @Test
    fun <A> `After remember should be showed remember right when passed as question`() {
        val words = QuizTest.dictionary.take(1)
        val manager = QuizManager(words.toSet())

        val cards = manager.takeAllUpToPredicate {it is Remember}
        @Suppress("UNCHECKED_CAST")
        val rememberCard = cards.last() as Question<A>
        val nextCard = manager.next(rememberCard, rememberCard.correctAnswer())

        assertEquals(RememberRight::class, nextCard!!::class)
        assertEquals(rememberCard.word, nextCard.word)
    }

    private fun QuizManager.takeAllWithCorrectAnswers(): List<Card> {
        val cards = mutableListOf<Card>()

        var card = start()
        cards.add(card!!)
        while (card != null) {
            card = nextWithRightAnswer(card)
            card?.let {  cards.add(card)}
        }
        return cards
    }


    private fun QuizManager.takeAllUpToPredicate(predicate: Predicate<Card>): List<Card> {
        val cards = mutableListOf<Card>()
        var card = start()
        cards.add(card!!)
        while (card != null && !predicate.test(card)) {
            card = nextWithRightAnswer(card)
            cards.add(card!!)
        }
        return cards
    }

    private fun QuizManager.nextWithRightAnswer(card: Card): Card? = when (card) {
        is Meaning -> {
            next(card)
        }
        is Remember -> {
            next(card, card.correctAnswer())
        }
        is Question<*> -> {
            @Suppress("UNCHECKED_CAST")
            val question: Question<Any> = card as Question<Any>
            next(question, question.correctAnswer())
        }
        else -> throw IllegalStateException("Unknown card type ${card::class}")
    }

    class QuestionWithIncorrectAnswer(
        private val card: Card,
        override val word: Word = card.word
    ) : Question<Any> {

        override fun checkAnswer(value: Any): Boolean = false

        override fun correctAnswer(): Any = Any()

        override fun type(): Card.Type = card.type()
    }

}
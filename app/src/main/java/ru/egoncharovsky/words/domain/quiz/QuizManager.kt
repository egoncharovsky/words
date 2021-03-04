package ru.egoncharovsky.words.domain.quiz

import mu.KotlinLogging
import ru.egoncharovsky.words.domain.Word
import ru.egoncharovsky.words.domain.quiz.card.*
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class QuizManager(
    val words: Set<Word>
) {
    val windowSize = 7
    val progressLimit = 6
    val minDistance = 2

    val questionDifficulty: Map<KClass<out Question<out Any>>, Question.Difficulty> = mapOf(
        Answer::class to Question.Difficulty.HIGH,
        MultiChoice::class to Question.Difficulty.MEDIUM,
        Remember::class to Question.Difficulty.LOW
    )

    private val logger = KotlinLogging.logger {}

    private val shuffler: WordShuffler = WordShuffler(words, windowSize, progressLimit, minDistance)
    private val cardsHistory: MutableList<Card> = mutableListOf()

    fun hasNext(): Boolean = shuffler.hasNext()

    fun start(): Card = logCard(next())

    fun <A> next(question: Question<A>, answer: A): Card {
        val correct = question.checkAnswer(answer)

        logger.trace("Answered: $answer (correct: $correct) to $question")

        val card = if (correct) {
            when(question) {
                is Remember -> RememberRight(question.word)
                else -> next()
            }
        } else {
            repeat(question.word)
        }

        return logCard(card)
    }

    fun next(meaning: Meaning): Card = logCard(next())

    fun progressPercentage(): Int = shuffler.totalProgressPercentage()

    fun logCard(card: Card): Card {
        cardsHistory.add(card)
        logger.debug("Next card is $card")
        return card
    }

    private fun repeat(word: Word): Meaning {
        shuffler.decrementProgress(word)
        logger.trace("Repeat $word")

        return Meaning(word)
    }

    private fun next(): Card {
        val word = shuffler.next()

        logger.trace("New $word")
        return when (shuffler.progressOf(word)) {
            1 -> Meaning(word)
            in (2..3) -> questionCard(randomQuestion(Question.Difficulty.LOW), word)
            in (4..5) -> questionCard(randomQuestion(Question.Difficulty.MEDIUM), word)
            else -> questionCard(randomQuestion(Question.Difficulty.HIGH), word)
        }
    }

    private fun questionCard(type: KClass<out Question<out Any>>, word: Word): Card {
        return when(type) {
            Answer::class -> Answer(word, word.translation)
            MultiChoice::class -> {
                val options = shuffler
                    .returned().minus(word).shuffled().take(4)
                    .plus(word).shuffled()
                    .map { it.translation }
                return MultiChoice(word, options, word.translation)
            }
            Remember::class -> Remember(word)
            else -> throw IllegalArgumentException("Unsupported question type $type")
        }
    }

    private fun randomQuestion(difficulty: Question.Difficulty) =
        questionDifficulty.filterValues { it == difficulty }.keys.random()

}
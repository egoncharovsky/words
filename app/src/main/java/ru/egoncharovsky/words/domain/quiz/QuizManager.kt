package ru.egoncharovsky.words.domain.quiz

import mu.KotlinLogging
import ru.egoncharovsky.words.domain.Word
import ru.egoncharovsky.words.domain.quiz.card.*
import kotlin.reflect.KClass

class QuizManager(
    val words: Set<Word>
) {
    val windowSize = 5
    val progressLimit = 4
    val minDistance = 2

    val questionDifficulty: Map<KClass<out Question<out Any>>, Question.Difficulty> = mapOf(
        Answer::class to Question.Difficulty.HIGH,
        MultiChoice::class to Question.Difficulty.MEDIUM,
        Remember::class to Question.Difficulty.LOW
    )

    private val logger = KotlinLogging.logger {}

    private val shuffler: WordShuffler = WordShuffler(words, windowSize, progressLimit, minDistance)
    private val cardsHistory: MutableList<Card> = mutableListOf()

    fun start(): Card? = logCard(next())

    fun <A> next(question: Question<A>, answer: A): Card? {
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

    fun next(meaning: Meaning): Card? = logCard(next())

    fun progressPercentage(): Int = shuffler.totalProgressPercentage()

    private fun logCard(card: Card?): Card? {
        card?.let {
            cardsHistory.add(card)
            logger.debug("Progress ${progressPercentage()} next card is $card")
        } ?: run {
            logger.debug("Quiz finished")
        }

        return card
    }

    private fun repeat(word: Word): Meaning {
        shuffler.decrementProgress(word)
        logger.trace("Repeat $word")

        return Meaning(word)
    }

    private fun next(): Card? {
        return if (shuffler.hasNext()) {
            val word = shuffler.next()

            logger.trace("New $word")
            when (shuffler.progressOf(word)) {
                1 -> Meaning(word)
                2 -> questionCard(randomQuestion(Question.Difficulty.LOW), word)
                3 -> questionCard(randomQuestion(Question.Difficulty.MEDIUM), word)
                else -> questionCard(randomQuestion(Question.Difficulty.HIGH), word)
            }
        } else null
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
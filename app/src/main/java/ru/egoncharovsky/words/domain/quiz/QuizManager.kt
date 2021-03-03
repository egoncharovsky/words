package ru.egoncharovsky.words.domain.quiz

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

    private val shuffler: WordShuffler = WordShuffler(words, windowSize, progressLimit, minDistance)

    private val cardsHistory: MutableList<Card> = mutableListOf()

    fun start(): Card {
        val card = next()

        cardsHistory.add(card)
        return card
    }

    fun <A> next(question: Question<A>, answer: A): Card {
        val correct = question.checkAnswer(answer)
        val card = if (correct) {
            next()
            // return new word
        } else {
            val word = question.word
            shuffler.decrementProgress(word)

            Meaning(word)
        }

        cardsHistory.add(card)
        return card
    }

    fun next(meaning: Meaning): Card {
        val card = next()

        cardsHistory.add(card)
        return card
    }

    private fun next(): Card {
        val word = shuffler.next()
        val progress = shuffler.progressOf(word)

        return when (progress) {
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
                val options = shuffler.returned().shuffled().take(4)
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
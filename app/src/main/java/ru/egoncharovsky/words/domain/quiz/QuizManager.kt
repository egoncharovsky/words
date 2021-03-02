package ru.egoncharovsky.words.domain.quiz

import ru.egoncharovsky.words.domain.Word
import ru.egoncharovsky.words.domain.quiz.card.*
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class QuizManager(
    val words: Set<Word>
) : Iterator<Card> {
    private val shuffler: WordShuffler = WordShuffler(words, 7, 5, 2)

    private val questionDifficulty: Map<KClass<out Question<out Any>>, Question.Difficulty> = mapOf(
        Answer::class to Question.Difficulty.HIGH,
        MultiChoice::class to Question.Difficulty.MEDIUM,
        Remember::class to Question.Difficulty.LOW
    )

    private val cardsHistory: MutableList<Card> = mutableListOf()
    private var repeatLastWord: Boolean = false

    override fun hasNext(): Boolean = shuffler.hasNext()

    override fun next(): Card {
        val card = if (repeatLastWord) {
            repeatLastWord = false

            Meaning(cardsHistory.last().word)
        } else {
            val word = shuffler.next()
            val progress = shuffler.progressOf(word)

            when (progress) {
                0 -> Meaning(word)
                in (1..2) -> questionCard(randomQuestion(Question.Difficulty.LOW), word)
                in (3..4) -> questionCard(randomQuestion(Question.Difficulty.MEDIUM), word)
                else -> questionCard(randomQuestion(Question.Difficulty.HIGH), word)
            }
        }

        cardsHistory.add(card)

        return card
    }

    fun answerCheckResult(result: Boolean) {
        if (!result) {
            shuffler.incorrectAnswer(cardsHistory.last().word)
            repeatLastWord = true
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
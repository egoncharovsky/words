package ru.egoncharovsky.words.domain.quiz.card

import ru.egoncharovsky.words.domain.Word

data class Remember(
    override val word: Word,
): Question<Remember.Option> {
    enum class Option {
        NO, MAYBE, YES
    }

    override fun checkAnswer(value: Option): Boolean = Option.YES == value
}



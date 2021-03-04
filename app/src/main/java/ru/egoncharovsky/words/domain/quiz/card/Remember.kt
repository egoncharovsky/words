package ru.egoncharovsky.words.domain.quiz.card

import ru.egoncharovsky.words.domain.Word

data class Remember(
    override val word: Word
): Question<Remember.Option> {
    enum class Option {
        NO, MAYBE, YES
    }

    override fun checkAnswer(value: Option): Boolean = correctAnswer() == value
    override fun correctAnswer(): Option = Option.YES

    override fun type(): Card.Type = Card.Type.REMEMBER

}

data class RememberRight(
    override val word: Word
): Question<RememberRight.Option> {
    enum class Option {
        NO, YES
    }

    override fun type(): Card.Type = Card.Type.REMEMBER_RIGHT

    override fun checkAnswer(value: Option): Boolean = correctAnswer() == value

    override fun correctAnswer(): Option = Option.YES

}



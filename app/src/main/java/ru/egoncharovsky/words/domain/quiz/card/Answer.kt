package ru.egoncharovsky.words.domain.quiz.card

import ru.egoncharovsky.words.domain.Word

data class Answer(
    override val word: Word,
    val correct: String,

    ) : Question<String> {

    override fun checkAnswer(value: String): Boolean = correctAnswer().trim().equals(value.trim(), ignoreCase = true)

    override fun correctAnswer(): String = correct
}
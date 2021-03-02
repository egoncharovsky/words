package ru.egoncharovsky.words.domain.quiz.card

import ru.egoncharovsky.words.domain.Word

data class MultiChoice(
    override val word: Word,
    val options: List<String>,
    val correct: String,

    ) : Question<String> {

    override fun checkAnswer(value: String): Boolean = correct.trim().equals(value.trim(), ignoreCase = true)
}

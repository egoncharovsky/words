package ru.egoncharovsky.words.domain.quiz.card

import ru.egoncharovsky.words.domain.Word

interface Card {
    enum class Type {
        ANSWER, MEANING, MULTI_CHOICE, REMEMBER, REMEMBER_RIGHT
    }

    val word: Word

    fun type(): Type
}
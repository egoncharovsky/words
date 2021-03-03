package ru.egoncharovsky.words.domain.quiz.card

interface Question<A> : Card {
    enum class Difficulty : Comparable<Difficulty> {
        LOW, MEDIUM, HIGH
    }

    fun checkAnswer(value: A): Boolean

    fun correctAnswer(): A
}
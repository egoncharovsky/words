package ru.egoncharovsky.words.domain.quiz.card

import ru.egoncharovsky.words.domain.Word

data class Meaning(
    override val word: Word
) : Card

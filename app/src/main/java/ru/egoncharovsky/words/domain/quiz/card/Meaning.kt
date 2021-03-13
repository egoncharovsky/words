package ru.egoncharovsky.words.domain.quiz.card

import ru.egoncharovsky.words.domain.entity.Word

data class Meaning(
    override val word: Word
) : Card {
    override fun type(): Card.Type = Card.Type.MEANING
}

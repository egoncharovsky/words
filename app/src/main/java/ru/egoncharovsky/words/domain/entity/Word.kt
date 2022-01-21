package ru.egoncharovsky.words.domain.entity

import java.time.LocalDateTime

data class Word(
    override val id: Long? = null,
    val createdAt: LocalDateTime? = null,

    val value: String,
    val translation: String,
    val language: Language,
    val translationLanguage: Language
) : Identifiable<Word, Long> {

    constructor(
        value: String,
        translation: String,
        language: Language,
        translationLanguage: Language
    ) : this(
        null,
        null,
        value,
        translation,
        language,
        translationLanguage
    )

    fun invert(): Word = Word(
        value = translation,
        translation = value,
        language = translationLanguage,
        translationLanguage = language
    )
}

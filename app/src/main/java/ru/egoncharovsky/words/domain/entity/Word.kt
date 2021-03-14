package ru.egoncharovsky.words.domain.entity

data class Word(
    override val id: Long? = null,

    val value: String,
    val translation: String,
    val language: Language,
    val translationLanguage: Language
) : Identifiable<Word, Long> {

    fun invert(): Word = Word(
        value = translation,
        translation = value,
        language = translationLanguage,
        translationLanguage = language
    )
}

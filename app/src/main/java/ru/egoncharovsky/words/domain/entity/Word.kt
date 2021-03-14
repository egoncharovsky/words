package ru.egoncharovsky.words.domain.entity

data class Word(
        val value: String,
        val translation: String,
        val language: Language,
        val translationLanguage: Language
) {

        fun invert(): Word = Word(
                value = translation,
                translation = value,
                language = translationLanguage,
                translationLanguage = language
        )
}

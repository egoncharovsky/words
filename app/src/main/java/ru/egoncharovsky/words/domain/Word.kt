package ru.egoncharovsky.words.domain

data class Word(
        val value: String,
        val translation: String,
        val language: Language,
        val translationLanguage: Language
) {
        fun invert():Word = Word(translation, value, translationLanguage, language)
}

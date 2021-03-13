package ru.egoncharovsky.words.domain.entity

data class Word(
        override val id: Long? = null,

        val value: String,
        val translation: String,
        val language: Language,
        val translationLanguage: Language
) : Entity<Word, Long> {
        override fun copy(newId: Long): Word = copy(id = id)

        fun invert(): Word = Word(
                value = translation,
                translation = value,
                language = translationLanguage,
                translationLanguage = language
        )
}

package ru.egoncharovsky.words.domain

data class DictionaryEntry(
    override var id: Long?,
    val word: Word
) : Entity<Long>

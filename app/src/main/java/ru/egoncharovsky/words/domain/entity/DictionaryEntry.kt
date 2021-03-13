package ru.egoncharovsky.words.domain.entity

data class DictionaryEntry(
    override var id: Long?,
    val word: Word
) : Entity<Long>

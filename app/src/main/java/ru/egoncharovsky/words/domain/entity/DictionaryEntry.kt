package ru.egoncharovsky.words.domain.entity

import ru.egoncharovsky.words.domain.Word

data class DictionaryEntry(
    override var id: Long?,
    val word: Word
) : Entity<Long>

package ru.egoncharovsky.words.domain

data class WordList(
    override var id: Long?,

    val name: String,
    val words: Set<Word>
) : Entity<Long>

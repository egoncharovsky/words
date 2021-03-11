package ru.egoncharovsky.words.domain

data class StudyList(
    override var id: Long?,

    val name: String,
    val words: Set<DictionaryEntry>
) : Entity<Long>

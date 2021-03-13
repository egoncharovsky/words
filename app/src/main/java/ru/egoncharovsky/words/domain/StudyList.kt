package ru.egoncharovsky.words.domain

data class StudyList(
    override var id: Long? = null,

    val name: String,
    val words: Set<Word>
) : Entity<Long>

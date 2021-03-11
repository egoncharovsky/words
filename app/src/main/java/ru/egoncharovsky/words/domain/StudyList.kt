package ru.egoncharovsky.words.domain

data class StudyList(
    override var id: Long?,

    var name: String,
    var dictionaryEntries: Set<DictionaryEntry>
) : Entity<Long>

package ru.egoncharovsky.words.domain.entity

data class StudyList(
    override val id: Long? = null,

    val name: String,
    val words: Set<Word>
) : Entity<StudyList, Long> {
    override fun copy(newId: Long): StudyList = copy(id = newId)
}

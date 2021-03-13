package ru.egoncharovsky.words.domain.entity

data class DictionaryEntry(
    override val id: Long?,
    val word: Word
) : Entity<DictionaryEntry, Long> {
    override fun copy(newId: Long): DictionaryEntry = copy(id = newId)
}

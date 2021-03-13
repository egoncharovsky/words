package ru.egoncharovsky.words.repository

import ru.egoncharovsky.words.domain.StudyList

object StudyListRepository : InMemoryRepository<Long, StudyList>(LongIdGenerator()) {

    init {
        listOf(
            StudyList(null, "Example List", takeWords(20)),
            StudyList(null, "Example List 2", takeWords(7)),
            StudyList(null, "Example List 3", takeWords(13))
        ).forEach { save(it) }
    }

    private fun takeWords(count: Int) = DictionaryEntryRepository.getAll()
        .shuffled().take(count).map { it.word }.toSet()
}
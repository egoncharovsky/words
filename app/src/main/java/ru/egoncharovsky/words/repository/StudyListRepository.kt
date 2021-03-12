package ru.egoncharovsky.words.repository

import ru.egoncharovsky.words.domain.StudyList

object StudyListRepository : InMemoryRepository<Long, StudyList>(LongIdGenerator()) {

    init {
        listOf(
            StudyList(null, "Example List", DictionaryEntryRepository.getAll().take(20).toSet()),
            StudyList(null, "Example List 2", DictionaryEntryRepository.getAll().take(7).toSet()),
            StudyList(null, "Example List 3", DictionaryEntryRepository.getAll().take(13).toSet())
        ).forEach { save(it) }
    }

}
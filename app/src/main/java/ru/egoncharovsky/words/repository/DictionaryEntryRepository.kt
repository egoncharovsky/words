package ru.egoncharovsky.words.repository

import ru.egoncharovsky.words.domain.DictionaryEntry

object DictionaryEntryRepository : InMemoryRepository<Long, DictionaryEntry>(LongIdGenerator())
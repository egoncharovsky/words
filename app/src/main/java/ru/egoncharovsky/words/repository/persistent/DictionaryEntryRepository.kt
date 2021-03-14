package ru.egoncharovsky.words.repository.persistent

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.egoncharovsky.words.database.dao.DictionaryEntryDao
import ru.egoncharovsky.words.domain.entity.DictionaryEntry
import ru.egoncharovsky.words.domain.entity.Word
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionaryEntryRepository @Inject constructor(
    private val dao: DictionaryEntryDao
) : BaseRepository<Long, DictionaryEntry>() {

    override fun getAll(): Flow<List<DictionaryEntry>> = dao.getAll().map { list ->
        list.map { DictionaryEntry(
            it.dictionaryEntry.id,
            Word(
                it.word.id,
                it.word.value,
                it.word.translation,
                it.word.language,
                it.word.translationLanguage
            )
        ) }
    }

    override fun find(id: Long): Flow<DictionaryEntry?> = TODO()

    override suspend fun saveAll(entities: Collection<DictionaryEntry>): List<Long> = TODO()

    override suspend fun delete(entity: DictionaryEntry) = TODO()

    override suspend fun deleteAll() = TODO()

    override suspend fun insert(entity: DictionaryEntry): Long = TODO()

    override suspend fun update(entity: DictionaryEntry): Int = TODO()

    fun searchWord(value: String): Flow<List<DictionaryEntry>> {
        TODO()
//        return dao.searchWord("%$value%")
    }

}
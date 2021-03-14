package ru.egoncharovsky.words.repository.persistent

import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.database.dao.DictionaryEntryDao
import ru.egoncharovsky.words.domain.entity.DictionaryEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionaryEntryRepository @Inject constructor(
    private val dao: DictionaryEntryDao
) : BaseRepository<Long, DictionaryEntry>() {

    override fun getAll(): Flow<List<DictionaryEntry>> = dao.getAll()

    override fun find(id: Long): Flow<DictionaryEntry?> = dao.find(id)

    override suspend fun saveAll(entities: Collection<DictionaryEntry>): List<Long> = dao.insertAll(entities)

    override suspend fun delete(entity: DictionaryEntry) = dao.delete(entity)

    override suspend fun deleteAll() = dao.deleteAll()

    override suspend fun insert(entity: DictionaryEntry): Long = dao.insert(entity)

    override suspend fun update(entity: DictionaryEntry): Int = dao.update(entity)

    fun searchWord(value: String): Flow<List<DictionaryEntry>> {
        return dao.searchWord("%$value%")
    }

}
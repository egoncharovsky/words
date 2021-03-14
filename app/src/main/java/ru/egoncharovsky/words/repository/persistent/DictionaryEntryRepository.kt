package ru.egoncharovsky.words.repository.persistent

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.egoncharovsky.words.database.dao.DictionaryEntryDao
import ru.egoncharovsky.words.database.tables.DictionaryEntryWord
import ru.egoncharovsky.words.domain.entity.DictionaryEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionaryEntryRepository @Inject constructor(
    private val dao: DictionaryEntryDao
) : BaseRepository<Long, DictionaryEntry>() {

    override fun getAll(): Flow<List<DictionaryEntry>> = dao.getAll().map { list ->
        list.map { it.toEntity() }
    }

    override fun find(id: Long): Flow<DictionaryEntry?> = dao.find(id).map { it?.toEntity() }

    override suspend fun saveAll(entities: Collection<DictionaryEntry>): List<Long> {
        return dao.insertAll(entities.map { DictionaryEntryWord.fromEntity(it) })
    }

    override suspend fun delete(entity: DictionaryEntry) = TODO()

    override suspend fun deleteAll() = TODO()

    override suspend fun insert(entity: DictionaryEntry): Long = TODO()

    override suspend fun update(entity: DictionaryEntry): Int = TODO()

    fun searchWord(value: String): Flow<List<DictionaryEntry>> {
        TODO()
//        return dao.searchWord("%$value%")
    }

}
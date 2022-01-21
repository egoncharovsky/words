package ru.egoncharovsky.words.repository.persistent.room

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mu.KotlinLogging
import ru.egoncharovsky.words.database.AppDatabase
import ru.egoncharovsky.words.database.dao.WordDao
import ru.egoncharovsky.words.database.tables.WordTable
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.repository.persistent.WordRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordRepositoryRoom @Inject constructor(
    private val dao: WordDao,
    private val database: AppDatabase
) : WordRepository {

    private val logger = KotlinLogging.logger { }

    override fun getAll(): Flow<List<Word>> {
        return dao.getAll().map { l -> l.map { it.toEntity() } }
    }

    override fun get(ids: Set<Long>): Flow<List<Word>> {
        return dao.get(ids).map { l -> l.map { it.toEntity() } }
    }

    override fun searchWord(value: String): Flow<List<Word>> {
        logger.trace("Search '$value'")
        return dao.searchWords("%$value%").map { l -> l.map { it.toEntity() } }
    }

    override fun searchNotIncludedInStudyLists(value: String): Flow<List<Word>> {
        logger.trace("Search not included in study lists '$value'")
        return dao.searchNotIncludedInStudyLists("%$value%").map { l -> l.map { it.toEntity() } }
    }

    override fun searchInWordsWithIds(value: String, ids: Set<Long>): Flow<List<Word>> {
        logger.trace("Search '$value' in words $ids")
        return dao.searchInWordsWithIds("%$value%", ids).map { l -> l.map { it.toEntity() } }
    }

    override suspend fun saveImportedWords(words: List<Word>): List<Long> {
        return database.withTransaction {
            words.mapNotNull { word ->
                val found = dao.find(word.value, word.translation)
                if (found == null) {
                    dao.insert(WordTable.fromEntity(word))
                } else null
            }
        }
    }

    override fun findNotIncludedInStudyLists(): Flow<List<Word>> {
        return dao.findNotIncludedInStudyLists().map { l -> l.map { it.toEntity() } }
    }

    override fun findWordsIdsIncludedInStudyListsExcluding(studyListId: Long?): Flow<Set<Long>> {
        return (studyListId?.let {
            dao.findWordsIdsIncludedInStudyListsExcluding(it)
        } ?: run {
            dao.findWordsIdsIncludedInStudyLists()
        })
            .map { it.toSet() }
    }
}
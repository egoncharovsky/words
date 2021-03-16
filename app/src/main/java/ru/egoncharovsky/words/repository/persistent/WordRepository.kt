package ru.egoncharovsky.words.repository.persistent

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mu.KotlinLogging
import ru.egoncharovsky.words.database.AppDatabase
import ru.egoncharovsky.words.database.dao.WordDao
import ru.egoncharovsky.words.database.tables.WordTable
import ru.egoncharovsky.words.domain.entity.Word
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordRepository @Inject constructor(
    private val dao: WordDao,
    private val database: AppDatabase
) {

    private val logger = KotlinLogging.logger { }

    fun getAll(): Flow<List<Word>> {
        return dao.getAll().map { l -> l.map { it.toEntity() } }
    }

    fun get(ids: Set<Long>): Flow<List<Word>> {
        return dao.get(ids).map { l -> l.map { it.toEntity() } }
    }

    fun searchWord(value: String): Flow<List<Word>> {
        logger.trace("Search $value")
        return dao.searchWords("%$value%").map { l -> l.map { it.toEntity() } }
    }

    suspend fun saveImportedWords(words: Set<Word>): List<Long> {
        return database.withTransaction {
            words.mapNotNull { word ->
                logger.trace("Find $word")
                val found = dao.find(word.value, word.translation)
                if (found == null) {
                    logger.trace("Word $word not found")
                    dao.insert(WordTable.fromEntity(word))
                } else null
            }
        }
    }
}
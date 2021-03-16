package ru.egoncharovsky.words.repository.persistent

import androidx.room.Transaction
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mu.KotlinLogging
import ru.egoncharovsky.words.database.AppDatabase
import ru.egoncharovsky.words.database.dao.DictionaryEntryDao
import ru.egoncharovsky.words.database.tables.DictionaryEntryTable
import ru.egoncharovsky.words.domain.entity.DictionaryEntry
import ru.egoncharovsky.words.domain.entity.Word
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionaryEntryRepository @Inject constructor(
    private val dao: DictionaryEntryDao,
    private val database: AppDatabase,
    private val wordRepository: WordRepository
) {

    private val logger = KotlinLogging.logger {}

    @Transaction
    fun getAll(): Flow<List<DictionaryEntry>> = dao.getAll().map { list ->
        list.map { it.toEntity() }
    }

    suspend fun saveImportedWords(words: Set<Word>): List<Long> {
        return database.withTransaction {
            val entriesForSave = words.map { word ->
                val found = wordRepository.find(word.value, word.translation)
                if (found == null) {
                    logger.trace("Word $word not found")
                    wordRepository.insert(word)
                } else {
                    logger.trace("Word $word found as $found")
                    found.id!!
                }
            }.mapNotNull { wordId ->
                if (!dao.isExist(wordId)) {
                    DictionaryEntryTable(
                        wordId = wordId
                    )
                } else {
                    null
                }
            }

            logger.debug("Inserting total DictionaryEntry ${entriesForSave.size}")

            dao.insertAll(entriesForSave)
        }
    }

    fun searchWord(value: String): Flow<List<DictionaryEntry>> {
        logger.trace("Search $value")
        return dao.searchWord("%$value%").map { list -> list.map { it.toEntity() } }
    }

    suspend fun findDictionaryEntryIds(words: Set<Word>): List<Long> {
        if (words.isEmpty()) return listOf()

        val entries = dao.findByWordIds(words.map { it.id!! }.toSet())

        return entries.map { it.dictionaryEntry.id!! }
    }

    fun getWords(dictionaryEntryIds: Set<Long>): Flow<Set<Word>> {
        return dao.findByIds(dictionaryEntryIds).map { l -> l.map { it.word.toEntity() }.toSet() }
    }
}
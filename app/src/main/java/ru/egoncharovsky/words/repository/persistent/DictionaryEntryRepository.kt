package ru.egoncharovsky.words.repository.persistent

import androidx.room.Transaction
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mu.KotlinLogging
import ru.egoncharovsky.words.database.AppDatabase
import ru.egoncharovsky.words.database.dao.DictionaryEntryDao
import ru.egoncharovsky.words.database.tables.DictionaryEntryTable
import ru.egoncharovsky.words.database.tables.DictionaryEntryWordJoin
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
        list.map { toEntity(it) }
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
        TODO()
        //        return dao.searchWord("%$value%")
    }

    private fun toEntity(join: DictionaryEntryWordJoin) = DictionaryEntry(
        join.dictionaryEntry.id,
        Word(
            join.word.id,
            join.word.value,
            join.word.translation,
            join.word.language,
            join.word.translationLanguage
        )
    )

    private fun fromEntity(entity: DictionaryEntry) = DictionaryEntryTable(
        entity.id,
        entity.word.id!!
    )
}
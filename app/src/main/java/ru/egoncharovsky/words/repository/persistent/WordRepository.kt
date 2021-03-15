package ru.egoncharovsky.words.repository.persistent

import mu.KotlinLogging
import ru.egoncharovsky.words.database.dao.WordDao
import ru.egoncharovsky.words.database.tables.WordTable
import ru.egoncharovsky.words.domain.entity.Word
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordRepository @Inject constructor(
    private val dao: WordDao
) {

    private val logger = KotlinLogging.logger { }

    suspend fun find(value: String, translation: String): Word? {
        logger.trace("Find word: value:$value translation:$translation")
        return dao.find(value, translation)?.let {
            Word(
                it.id,
                it.value,
                it.translation,
                it.language,
                it.translationLanguage
            )
        }
    }

    suspend fun insert(word: Word): Long {
        logger.trace("Insert $word")
        return dao.insert(
            WordTable(
                word.id,
                word.value,
                word.translation,
                word.language,
                word.translationLanguage
            )
        )
    }
//    fun find(value: String, translation: String): Flow<Word?> = dao.find(value, translation)
}
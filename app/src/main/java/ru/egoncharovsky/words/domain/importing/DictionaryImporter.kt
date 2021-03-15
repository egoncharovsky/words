package ru.egoncharovsky.words.domain.importing

import mu.KotlinLogging
import ru.egoncharovsky.words.domain.entity.Language
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.repository.persistent.DictionaryEntryRepository
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset


class DictionaryImporter(
    private val dictionaryEntryRepository: DictionaryEntryRepository
) {

    private val logger = KotlinLogging.logger {}

    suspend fun import(csvIS: InputStream) {
        logger.debug("Import started")
        val words = readWords(csvIS.reader(Charset.defaultCharset()))

        logger.debug("Saving ${words.size} words")
        val saved = dictionaryEntryRepository.saveImportedWords(words)
        logger.debug("Saved ${saved.size} new dictionary entries")
    }

    fun readWords(reader: InputStreamReader): Set<Word> = reader.useLines { lines ->
        lines.map { it.split(',') }.mapNotNull { line ->
            val languageFrom = line[0]
            val languageTo = line[1]
            val value = line[2]
            val translation = line[3]

            val word = Word(
                value = value,
                translation = translation,
                language = Language.EN,
                translationLanguage = Language.RU
            )
            if (languageFrom == "английский" && languageTo == "русский") {
                word
            } else if (languageFrom == "русский" && languageTo == "английский") {
                word.invert()
            } else {
                logger.error("Ignore unsupported languages pair $languageFrom -> $languageTo for line $line")
                null
            }
        }.toSet()
    }
}
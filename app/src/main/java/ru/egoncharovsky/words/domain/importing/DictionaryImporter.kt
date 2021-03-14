package ru.egoncharovsky.words.domain.importing

import mu.KotlinLogging
import ru.egoncharovsky.words.domain.entity.DictionaryEntry
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
        val words = readWords(csvIS.reader(Charset.defaultCharset()))
        val dictionaryEntries = words.map { DictionaryEntry(null, it) }

        dictionaryEntryRepository.saveAll(dictionaryEntries)
    }

    fun readWords(reader: InputStreamReader): List<Word> = reader.useLines { lines ->
        lines.map { it.split(',') }.mapNotNull { line ->
            val languageFrom = line[0]
            val languageTo = line[1]
            val value = line[2]
            val translation = line[3]

            if (languageFrom == "английский" && languageTo == "русский") {
                Word(
                    value = value,
                    translation = translation,
                    language = Language.EN,
                    translationLanguage = Language.RU
                )
            } else if (languageFrom == "русский" && languageTo == "английский") {
                Word(
                    value = value,
                    translation = translation,
                    language = Language.EN,
                    translationLanguage = Language.RU
                ).invert()
            } else {
                logger.error("Ignore unsupported languages pair $languageFrom -> $languageTo for line $line")
                null
            }
        }.toList()
    }
}
package ru.egoncharovsky.words.domain.import

import mu.KotlinLogging
import ru.egoncharovsky.words.domain.Language
import ru.egoncharovsky.words.domain.Word
import ru.egoncharovsky.words.domain.entity.DictionaryEntry
import ru.egoncharovsky.words.repository.DictionaryEntryRepository
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

class DictionaryImporter {

    private val logger = KotlinLogging.logger {}

    fun import(csvIS: InputStream) {
        val words = readWords(csvIS.reader(Charset.defaultCharset()))
        val dictionaryEntries = words.map { DictionaryEntry(null, it) }

        DictionaryEntryRepository.saveAll(dictionaryEntries)
    }

    fun readWords(reader: InputStreamReader): List<Word> = reader.useLines { lines ->
        lines.map { it.split(',') }.mapNotNull { line ->
            val languageFrom = line[0]
            val languageTo = line[1]
            val value = line[2]
            val translation = line[3]

            if (languageFrom == "английский" && languageTo == "русский") {
                Word(value, translation, Language.EN, Language.RU)
            } else if (languageFrom == "русский" && languageTo == "английский") {
                Word(value, translation, Language.EN, Language.RU).invert()
            } else {
                logger.error("Ignore unsupported languages pair $languageFrom -> $languageTo for line $line")
                null
            }
        }.toList()
    }
}
package ru.egoncharovsky.words.domain.importing

import mu.KotlinLogging
import ru.egoncharovsky.words.repository.persistent.WordRepository
import java.io.InputStream
import java.nio.charset.Charset


class DictionaryImporter(
    private val wordRepository: WordRepository,
    private val csvParser: CsvParser
) {

    private val logger = KotlinLogging.logger {}

    suspend fun import(csvIS: InputStream) {
        logger.debug("Import started")
        val words = csvParser.readWords(csvIS.reader(Charset.defaultCharset()))

        logger.debug("Saving ${words.size} words")
        val saved = wordRepository.saveImportedWords(words)
        logger.debug("Saved ${saved.size} new words from ${words.size}")
    }
}
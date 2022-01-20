package ru.egoncharovsky.words.domain.importing

import mu.KotlinLogging
import ru.egoncharovsky.words.domain.entity.Language
import ru.egoncharovsky.words.domain.entity.Word
import java.io.InputStreamReader

class CsvParser {

    private val logger = KotlinLogging.logger {}

    fun readWords(reader: InputStreamReader): List<Word> = reader.useLines {
        it.mapNotNull(::parseCsvLine).toList()
    }

    fun parseCsvLine(line: String): Word? {
        val columns = line.split(",(?=([^\"]|\"[^\"]*\")*$)".toRegex())

        val languageFrom = columns[0].lowercase()
        val languageTo = columns[1].lowercase()
        val value = trimQuotes(columns[2]).lowercase()
        val translation = trimQuotes(columns[3]).lowercase()

        val word = Word(
            value = value,
            translation = translation,
            language = Language.EN,
            translationLanguage = Language.RU
        )
        if (languageFrom == "английский" && languageTo == "русский") {
            return word
        } else if (languageFrom == "русский" && languageTo == "английский") {
            return word.invert()
        } else {
            logger.error("Ignore unsupported languages pair $languageFrom -> $languageTo for line $line")
            return null
        }
    }

    private fun trimQuotes(value: String) = value.trim('"')
}
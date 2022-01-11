package ru.egoncharovsky.words.domain.importing

import mu.KotlinLogging
import ru.egoncharovsky.words.domain.entity.Language
import ru.egoncharovsky.words.domain.entity.Word
import java.io.InputStreamReader
import java.util.*

class CsvParser {

    private val logger = KotlinLogging.logger {}

    fun readWords(reader: InputStreamReader): Set<Word> = reader.useLines {
        it.mapNotNull(::parseCsvLine).toSet()
    }

    fun parseCsvLine(line: String): Word? {
        val columns = line.split(",(?=([^\"]|\"[^\"]*\")*$)".toRegex())

        val languageFrom = columns[0].toLowerCase(Locale.getDefault())
        val languageTo = columns[1].toLowerCase(Locale.getDefault())
        val value = trimQuotes(columns[2].toLowerCase(Locale.getDefault()))
        val translation = trimQuotes(columns[3].toLowerCase(Locale.getDefault()))

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
package ru.egoncharovsky.words.domain.importing

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.egoncharovsky.words.domain.entity.Language
import ru.egoncharovsky.words.domain.entity.Word

internal class CsvParserTest {

    private val parser = CsvParser()

    @Test
    fun parseCsvLine() {
        assertEquals(
            Word(null, "word", "слово", Language.EN, Language.RU),
            parser.parseCsvLine("английский,русский,word,слово")
        )
    }

    @Test
    fun `parse with spaces`() {
        assertEquals(
            Word(null, "word and translate", "слово и его перевод", Language.EN, Language.RU),
            parser.parseCsvLine("английский,русский,word and translate,\"слово и его перевод\"")
        )
    }

    @Test
    fun `parse with commas in quotes`() {
        assertEquals(
            Word(null, "word, and translate", "слово, и его перевод", Language.EN, Language.RU),
            parser.parseCsvLine("английский,русский,\"word, and translate\",\"слово, и его перевод\"")
        )
    }
}
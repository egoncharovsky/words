package ru.egoncharovsky.words.ui.import

import androidx.lifecycle.ViewModel
import ru.egoncharovsky.words.domain.import.DictionaryImporter
import java.io.InputStream

class ImportWordsViewModel : ViewModel() {

    private val importer: DictionaryImporter = DictionaryImporter()

    fun importCsv(stream: InputStream) {
        importer.import(stream)
    }
}
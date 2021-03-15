package ru.egoncharovsky.words.ui.importing

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import ru.egoncharovsky.words.domain.importing.DictionaryImporter
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ImportWordsViewModel @Inject constructor(
    private val importer: DictionaryImporter
) : ViewModel() {

    private val logger = KotlinLogging.logger {  }

    fun importCsv(stream: InputStream) {
        logger.debug("Import requested")
        GlobalScope.launch(Dispatchers.IO) {
            importer.import(stream)
        }
    }
}
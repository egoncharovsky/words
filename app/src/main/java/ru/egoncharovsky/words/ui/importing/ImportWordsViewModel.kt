package ru.egoncharovsky.words.ui.importing

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.egoncharovsky.words.domain.importing.DictionaryImporter
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ImportWordsViewModel @Inject constructor(
    private val importer: DictionaryImporter
) : ViewModel() {

    fun importCsv(stream: InputStream) {
        GlobalScope.launch {
            importer.import(stream)
        }
    }
}
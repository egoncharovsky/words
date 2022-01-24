package ru.egoncharovsky.words.ui.importing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import ru.egoncharovsky.words.database.AppDatabase
import ru.egoncharovsky.words.domain.importing.DictionaryImporter
import ru.egoncharovsky.words.domain.importing.WordPopularityImporter
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ImportWordsViewModel @Inject constructor(
    private val importer: DictionaryImporter,
    private val wordPopularityImporter: WordPopularityImporter,
    private val appDatabase: AppDatabase
) : ViewModel() {

    private val logger = KotlinLogging.logger {  }

    private val imported = MutableLiveData<Boolean>()
    private val popularityUpgraded = MutableLiveData<Boolean>()
    private val cleared = MutableLiveData<Boolean>()

    fun isImported(): LiveData<Boolean> = imported
    fun isPopularityUpgraded(): LiveData<Boolean> = popularityUpgraded
    fun isCleared(): LiveData<Boolean> = cleared

    @DelicateCoroutinesApi
    fun importCsv(stream: InputStream) {
        logger.debug("Import requested")
        GlobalScope.launch(Dispatchers.IO) {
            importer.import(stream)
            imported.postValue(true)
        }
    }

    @DelicateCoroutinesApi
    fun importWordPopularityStatistic(stream: InputStream) {
        logger.debug("Import words popularity requested")
        GlobalScope.launch(Dispatchers.IO) {
            wordPopularityImporter.upgradePopularity(stream)
            popularityUpgraded.postValue(true)
        }
    }

    @DelicateCoroutinesApi
    fun clearDatabase() {
        GlobalScope.launch(Dispatchers.IO) {
            appDatabase.clearAllTables()
            cleared.postValue(true)
        }
    }
}
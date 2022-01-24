package ru.egoncharovsky.words.ui.dictionary

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.repository.persistent.WordRepository
import ru.egoncharovsky.words.ui.dictionary.search.WordSearchViewModel
import javax.inject.Inject

@HiltViewModel
open class DictionaryViewModel @Inject constructor(
    private val repository: WordRepository
) : WordSearchViewModel(repository) {

    init {
        onInit()
    }

    override fun getAllRequest(): Flow<List<Word>> = repository.getAll()

    override fun searchWordRequest(value: String): Flow<List<Word>> = repository.searchWord(value)
}
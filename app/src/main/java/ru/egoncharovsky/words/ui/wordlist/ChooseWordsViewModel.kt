package ru.egoncharovsky.words.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.repository.persistent.WordRepository
import ru.egoncharovsky.words.ui.dictionary.search.WordSearchViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseWordsViewModel @Inject constructor(
    val wordRepository: WordRepository,
    savedStateHandle: SavedStateHandle
) : WordSearchViewModel() {

    private val chosenDictionaryIds: MutableLiveData<List<Long>> = MutableLiveData<List<Long>>()
    private val showAlreadyIncluded: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        value = false
    }

    init {
        selectionUpdated(savedStateHandle.get<LongArray>("chosen")!!.toList())
        onInit()
    }

    fun getChosenDictionaryIds(): LiveData<List<Long>> = chosenDictionaryIds

    fun selectionUpdated(ids: List<Long>) {
        if (ids != chosenDictionaryIds.value) {
            chosenDictionaryIds.value = ids
        }
    }

    fun searchFiltersUpdated(showAlreadyIncluded: Boolean, searchText: String) {
        if (showAlreadyIncluded != this.showAlreadyIncluded.value) {
            this.showAlreadyIncluded.value = showAlreadyIncluded
            search(searchText)
        }
    }

    override fun getAllRequest(): Flow<List<Word>> {
        return if (showAlreadyIncluded.value!!) {
            wordRepository.getAll()
        } else {
            wordRepository.findWithoutAlreadyIncluded(chosenDictionaryIds.value!!.toSet())
        }
    }

    override fun searchWordRequest(value: String): Flow<List<Word>> {
        return if (showAlreadyIncluded.value!!) {
            wordRepository.searchWord(value)
        } else {
            wordRepository.searchWordWithoutAlreadyIncluded(value, chosenDictionaryIds.value!!.toSet())
        }
    }
}
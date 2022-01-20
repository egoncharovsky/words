package ru.egoncharovsky.words.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.domain.service.WordService
import ru.egoncharovsky.words.repository.persistent.WordRepository
import ru.egoncharovsky.words.ui.NavArgLong
import ru.egoncharovsky.words.ui.dictionary.search.WordSearchViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseWordsViewModel @Inject constructor(
    val wordRepository: WordRepository,
    val wordService: WordService,
    savedStateHandle: SavedStateHandle
) : WordSearchViewModel() {

    private val studyListId: Long? = savedStateHandle.get<NavArgLong>("studyListId")?.value
    private val chosenDictionaryIds: MutableLiveData<List<Long>> = MutableLiveData<List<Long>>()
    private val showAlreadyIncluded: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        value = false
    }
    private val wordsIdsIncludedInAnotherLists: MutableLiveData<Set<Long>> = MutableLiveData<Set<Long>>().apply {
        value = setOf()
    }

    init {
        updateAlreadyIncluded()
        selectionUpdated(savedStateHandle.get<LongArray>("chosen")!!.toList())
        onInit()
    }

    fun getChosenDictionaryIds(): LiveData<List<Long>> = chosenDictionaryIds

    fun isIncludedInAnotherList(wordId: Long): Boolean {
        return wordsIdsIncludedInAnotherLists.value!!.contains(wordId)
    }

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

    private fun updateAlreadyIncluded() {
        viewModelScope.launch {
            wordRepository.findWordsIdsIncludedInStudyListsExcluding(studyListId).collect {
                wordsIdsIncludedInAnotherLists.postValue(it)
            }
        }
    }

    override fun getAllRequest(): Flow<List<Word>> {
        return if (showAlreadyIncluded.value!!) {
            wordRepository.getAll()
        } else {
            wordService.findHidingAlreadyIncluded(chosenDictionaryIds.value!!.toSet())
        }
    }

    override fun searchWordRequest(value: String): Flow<List<Word>> {
        return if (showAlreadyIncluded.value!!) {
            wordRepository.searchWord(value)
        } else {
            wordService.searchHidingAlreadyIncluded(value, chosenDictionaryIds.value!!.toSet())
        }
    }
}
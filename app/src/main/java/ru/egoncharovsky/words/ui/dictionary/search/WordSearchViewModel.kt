package ru.egoncharovsky.words.ui.dictionary.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.egoncharovsky.words.domain.entity.Word

abstract class WordSearchViewModel : ViewModel(), WordSearchableViewModel {

    private var request: Job? = null

    private val sort = MutableLiveData<WordSearchWidget.SortType>().apply {
        value = WordSearchWidget.SortType.DEFAULT
    }
    private val words: MutableLiveData<List<Word>> = MutableLiveData<List<Word>>()

    fun onInit() {
        request(getAllRequest())
    }

    override fun setSort(sortType: WordSearchWidget.SortType) {
        this.sort.value = sortType
    }

    override fun search(newText: String?) {
        newText?.trim()?.let { value ->
            if (value.isNotBlank()) {
                request(searchWordRequest(value))
            } else {
                cancelSearch()
            }
        }
    }

    override fun cancelSearch() {
        request(getAllRequest())
    }

    override fun onSortChanged() {
        words.value?.let {
            words.value = sorted(it)
        }
    }

    override fun getSort(): LiveData<WordSearchWidget.SortType>  = sort

    override fun getWords(): LiveData<List<Word>> = words

    abstract fun getAllRequest(): Flow<List<Word>>
    abstract fun searchWordRequest(value: String): Flow<List<Word>>

    private fun request(flow: Flow<List<Word>>) {
        request?.cancel()
        request = viewModelScope.launch {
            flow.map { sorted(it) }.collect {
                words.postValue(it)
            }
        }
    }
    private fun sorted(list: List<Word>) = sort.value!!.apply(list)
}
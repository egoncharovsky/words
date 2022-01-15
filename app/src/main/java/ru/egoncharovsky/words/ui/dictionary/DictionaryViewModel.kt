package ru.egoncharovsky.words.ui.dictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.repository.persistent.WordRepository
import ru.egoncharovsky.words.ui.dictionary.search.WordSearchViewModel
import ru.egoncharovsky.words.ui.dictionary.search.WordSearchWidget.SortType
import javax.inject.Inject

@HiltViewModel
open class DictionaryViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel(), WordSearchViewModel {

    private var request: Job? = null

    private val sort = MutableLiveData<SortType>().apply {
        value = SortType.DEFAULT
    }
    private val words: MutableLiveData<List<Word>> = MutableLiveData()

    init {
        request(repository.getAll())
    }

    override fun getSort(): LiveData<SortType> = sort
    override fun getWords(): LiveData<List<Word>> = words

    override fun cancelSearch() {
        request(repository.getAll())
    }

    override fun search(newText: String?) {
        newText?.trim()?.let { value ->
            if (value.isNotBlank()) {
                request(repository.searchWord(value))
            } else {
                cancelSearch()
            }
        }
    }

    override fun setSort(sortType: SortType) {
        this.sort.value = sortType
    }

    override fun onSortChanged() {
        words.value?.let {
             words.value = sorted(it)
        }
    }

    private fun sorted(list: List<Word>) = sort.value!!.apply(list)

    private fun request(flow: Flow<List<Word>>) {
        request?.cancel()
        request = viewModelScope.launch {
            flow.map { sorted(it) }.collect {
                words.postValue(it)
            }
        }
    }
}
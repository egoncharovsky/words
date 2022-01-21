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

    private val sort = MutableLiveData<SortType>().apply {
        value = SortType.DEFAULT
    }
    private val words: MutableLiveData<List<Word>> = MutableLiveData<List<Word>>()

    fun onInit() {
        request(getAllRequest())
    }

    override fun setSort(sortType: SortType) {
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

    override fun getSort(): LiveData<SortType> = sort

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

    enum class SortType {
        DEFAULT {
            override fun apply(list: List<Word>) = list.sortedBy { it.id }
        },
        WORD_VALUE_ASK {
            override fun apply(list: List<Word>) = list
                .sortedBy { it.value.lowercase() }
        },
        WORD_VALUE_DESC {
            override fun apply(list: List<Word>) =
                list.sortedByDescending { it.value.lowercase() }
        },
        WORD_UPLOAD_DATE_ASK {
            override fun apply(list: List<Word>): List<Word> =
                list.sortedWith(compareBy<Word> { it.createdAt }.thenBy { it.id })
        },
        WORD_UPLOAD_DATE_DESC {
            override fun apply(list: List<Word>): List<Word> =
                list.sortedWith(compareByDescending<Word> { it.createdAt }.thenByDescending { it.id })
        };

        abstract fun apply(list: List<Word>): List<Word>
    }
}
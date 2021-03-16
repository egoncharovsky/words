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
import java.util.*
import javax.inject.Inject

@HiltViewModel
open class DictionaryViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {

    private var request: Job? = null

    enum class SortType {
        DEFAULT {
            override fun apply(list: List<Word>) = list.sortedBy { it.id }
        },
        WORD_VALUE_ASK {
            override fun apply(list: List<Word>) = list
                .sortedBy { it.value.toLowerCase(Locale.ROOT) }
        },
        WORD_VALUE_DESC {
            override fun apply(list: List<Word>) =
                list.sortedByDescending { it.value.toLowerCase(Locale.ROOT) }
        };

        abstract fun apply(list: List<Word>): List<Word>
    }

    private val sort = MutableLiveData<SortType>().apply {
        value = SortType.DEFAULT
    }
    private val words: MutableLiveData<List<Word>> = MutableLiveData()

    init {
        request(repository.getAll())
    }

    fun getSort(): LiveData<SortType> = sort
    fun getWords(): LiveData<List<Word>> = words

    private fun sorted(list: List<Word>) = sort.value!!.apply(list)

    fun cancelSearch() {
        request(repository.getAll())
    }

    fun search(input: String?) {
        input?.trim()?.let { value ->
            if (value.isNotBlank()) {
                request(repository.searchWord(value))
            } else {
                cancelSearch()
            }
        }
    }

    fun setSort(sort: SortType) {
        this.sort.value = sort
    }

    fun onSortChanged() {
        words.value?.let {
             words.value = sorted(it)
        }
    }

    private fun request(flow: Flow<List<Word>>) {
        request?.cancel()
        request = viewModelScope.launch {
            flow.map { sorted(it) }.collect {
                words.postValue(it)
            }
        }
    }
}
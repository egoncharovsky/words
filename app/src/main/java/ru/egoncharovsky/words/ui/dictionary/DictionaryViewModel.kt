package ru.egoncharovsky.words.ui.dictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.egoncharovsky.words.domain.entity.DictionaryEntry
import ru.egoncharovsky.words.repository.persistent.DictionaryEntryRepository
import java.util.*
import javax.inject.Inject

@HiltViewModel
open class DictionaryViewModel @Inject constructor(
    private val repository: DictionaryEntryRepository
) : ViewModel() {

    enum class SortType {
        DEFAULT {
            override fun apply(list: List<DictionaryEntry>) = list.sortedBy { it.id }
        },
        WORD_VALUE_ASK {
            override fun apply(list: List<DictionaryEntry>): List<DictionaryEntry> = list
                .sortedBy { it.word.value.toLowerCase(Locale.ROOT) }
        },
        WORD_VALUE_DESC {
            override fun apply(list: List<DictionaryEntry>): List<DictionaryEntry> =
                list.sortedByDescending { it.word.value.toLowerCase(Locale.ROOT) }
        };

        abstract fun apply(list: List<DictionaryEntry>): List<DictionaryEntry>
    }

    private val sort = MutableLiveData<SortType>().apply {
        value = SortType.DEFAULT
    }
    val dictionaryEntries: MutableLiveData<List<DictionaryEntry>> = MutableLiveData()

    init {
        request(repository.getAll())
    }

    fun getSort(): LiveData<SortType> = sort

    private fun sorted(list: List<DictionaryEntry>) = sort.value!!.apply(list)

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
        dictionaryEntries.value?.let {
             dictionaryEntries.value = sorted(it)
        }
    }

    private fun request(flow: Flow<List<DictionaryEntry>>) = viewModelScope.launch {
        flow.map { sorted(it) }.collect {
            dictionaryEntries.postValue(it)
        }
    }
}
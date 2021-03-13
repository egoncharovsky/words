package ru.egoncharovsky.words.ui.dictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.egoncharovsky.words.domain.entity.DictionaryEntry
import ru.egoncharovsky.words.repository.DictionaryEntryRepository
import java.util.*

open class DictionaryViewModel : ViewModel() {

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

    protected val dictionaryEntries = MutableLiveData<List<DictionaryEntry>>().apply {
        value = SortType.DEFAULT.apply(DictionaryEntryRepository.getAll())
    }
    private val sort = MutableLiveData<SortType>().apply {
        value = SortType.DEFAULT
    }

    fun getDictionaryEntries(): LiveData<List<DictionaryEntry>> = dictionaryEntries
    fun getSort(): LiveData<SortType> = sort

    fun cancelSearch() {
        dictionaryEntries.value = sort.value!!.apply(DictionaryEntryRepository.getAll())
    }

    fun search(value: String?) {
        value?.trim()?.let {
            if (it.isNotBlank()) {
                dictionaryEntries.value = sort.value!!.apply(DictionaryEntryRepository.searchWord(it))
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
             dictionaryEntries.value = sort.value!!.apply(it)
        }
    }
}
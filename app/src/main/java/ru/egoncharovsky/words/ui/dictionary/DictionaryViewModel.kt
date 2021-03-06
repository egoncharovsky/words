package ru.egoncharovsky.words.ui.dictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.egoncharovsky.words.domain.DictionaryEntry
import ru.egoncharovsky.words.repository.DictionaryEntryRepository

class DictionaryViewModel : ViewModel() {

    enum class SortType {
        DEFAULT {
            override fun apply(list: List<DictionaryEntry>) = list.sortedBy { it.id }
        },
        WORD_VALUE_ASK {
            override fun apply(list: List<DictionaryEntry>): List<DictionaryEntry> = list.sortedBy { it.word.value }
        },
        WORD_VALUE_DESC {
            override fun apply(list: List<DictionaryEntry>): List<DictionaryEntry> =
                list.sortedByDescending { it.word.value }
        };

        abstract fun apply(list: List<DictionaryEntry>): List<DictionaryEntry>
    }

    private val dictionaryEntries = MutableLiveData<List<DictionaryEntry>>().apply {
        value = SortType.DEFAULT.apply(DictionaryEntryRepository.getAll())
    }
    private val sort = MutableLiveData<SortType>().apply {
        value = SortType.DEFAULT
    }

    fun getDictionaryEntries(): LiveData<List<DictionaryEntry>> = dictionaryEntries

    fun cancelSearch() {
        dictionaryEntries.value = sort.value!!.apply(DictionaryEntryRepository.getAll())
    }

    fun search(value: String) {
        value.trim().takeIf { it.isNotBlank() }?.let {
            dictionaryEntries.value = sort.value!!.apply(DictionaryEntryRepository.searchWord(it))
        }
    }

    fun sort(sort: SortType) {
        dictionaryEntries.value?.let {
            dictionaryEntries.value = sort.apply(it)
        }
    }

}
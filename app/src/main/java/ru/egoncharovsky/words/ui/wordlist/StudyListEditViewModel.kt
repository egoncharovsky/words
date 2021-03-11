package ru.egoncharovsky.words.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.egoncharovsky.words.domain.Word
import ru.egoncharovsky.words.repository.DictionaryEntryRepository

class StudyListEditViewModel : ViewModel() {

    private val dictionaryEntryIds: MutableLiveData<LongArray> = MutableLiveData<LongArray>()
    private val words: MutableLiveData<List<Word>> = MutableLiveData<List<Word>>().apply {
        value = listOf()
    }

    fun getWords(): LiveData<List<Word>> = words
    fun getDictionaryEntryIds(): LiveData<LongArray> = dictionaryEntryIds

    fun load(id: Long) {

    }

    fun wordsSelected(dictionaryEntryIds : LongArray) {
        this.dictionaryEntryIds.value = dictionaryEntryIds
        val words = dictionaryEntryIds.map { DictionaryEntryRepository.get(it).word }.toList()
        this.words.value = words
    }
}
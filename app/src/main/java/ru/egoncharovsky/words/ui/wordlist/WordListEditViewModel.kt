package ru.egoncharovsky.words.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.egoncharovsky.words.domain.Word

class WordListEditViewModel : ViewModel() {

    private val words: MutableLiveData<List<Word>> = MutableLiveData<List<Word>>().apply {
        value = listOf()
    }

    fun getWords(): LiveData<List<Word>> = words
}
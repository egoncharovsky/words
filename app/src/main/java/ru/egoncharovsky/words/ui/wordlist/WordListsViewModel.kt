package ru.egoncharovsky.words.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.egoncharovsky.words.domain.WordList

class WordListsViewModel : ViewModel() {

    private val wordLists: MutableLiveData<List<WordList>> = MutableLiveData<List<WordList>>().apply {
        value = (1..20).map { (WordList(1, "Name $it", setOf())) }.toList()
    }

    fun getWordLists(): LiveData<List<WordList>> = wordLists

}
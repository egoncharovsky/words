package ru.egoncharovsky.words.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.egoncharovsky.words.ui.dictionary.DictionaryViewModel

class ChooseWordsViewModel : DictionaryViewModel() {

    private val chosenDictionaryIds: MutableLiveData<List<Long>> = MutableLiveData()

    fun getChosenDictionaryIds(): LiveData<List<Long>> = chosenDictionaryIds

    fun selectionUpdated(ids: List<Long>) {
        if (ids != chosenDictionaryIds.value) {
            chosenDictionaryIds.value = ids
        }
    }
}
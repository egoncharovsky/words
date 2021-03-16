package ru.egoncharovsky.words.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.egoncharovsky.words.repository.persistent.WordRepository
import ru.egoncharovsky.words.ui.dictionary.DictionaryViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseWordsViewModel @Inject constructor(
    wordRepository: WordRepository
): DictionaryViewModel(wordRepository) {

    private val chosenDictionaryIds: MutableLiveData<List<Long>> = MutableLiveData()

    fun getChosenDictionaryIds(): LiveData<List<Long>> = chosenDictionaryIds

    fun selectionUpdated(ids: List<Long>) {
        if (ids != chosenDictionaryIds.value) {
            chosenDictionaryIds.value = ids
        }
    }
}
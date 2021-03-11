package ru.egoncharovsky.words.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.egoncharovsky.words.domain.StudyList
import ru.egoncharovsky.words.repository.StudyListRepository

class StudyListsViewModel : ViewModel() {

    private val studyLists: MutableLiveData<List<StudyList>> = MutableLiveData<List<StudyList>>()

    fun getWordLists(): LiveData<List<StudyList>> = studyLists

    fun load() {
        studyLists.value = StudyListRepository.getAll()
    }
}
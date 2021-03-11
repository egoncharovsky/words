package ru.egoncharovsky.words.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.egoncharovsky.words.domain.StudyList

class StudyListsViewModel : ViewModel() {

    private val studyLists: MutableLiveData<List<StudyList>> = MutableLiveData<List<StudyList>>().apply {
        value = (1..20).map { (StudyList(1, "Name $it", setOf())) }.toList()
    }

    fun getWordLists(): LiveData<List<StudyList>> = studyLists

}
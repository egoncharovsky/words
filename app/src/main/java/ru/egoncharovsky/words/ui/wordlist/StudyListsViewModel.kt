package ru.egoncharovsky.words.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.egoncharovsky.words.domain.entity.StudyList
import ru.egoncharovsky.words.repository.persistent.StudyListRepository
import javax.inject.Inject

@HiltViewModel
class StudyListsViewModel @Inject constructor(
    private val studyListRepository: StudyListRepository
) : ViewModel() {

    val studyLists: LiveData<List<StudyList>> = studyListRepository.getAll().asLiveData()
}
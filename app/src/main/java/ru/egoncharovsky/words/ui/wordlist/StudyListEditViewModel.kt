package ru.egoncharovsky.words.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.egoncharovsky.words.domain.entity.StudyList
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.repository.persistent.StudyListRepository
import ru.egoncharovsky.words.repository.persistent.WordRepository
import javax.inject.Inject

@HiltViewModel
class StudyListEditViewModel @Inject constructor(
    private val studyListRepository: StudyListRepository,
    private val wordRepository: WordRepository
) : ViewModel() {

    private val dataLoaded = MutableLiveData<Boolean>()

    private val studyList: MutableLiveData<StudyList> = MutableLiveData()

    private val name: MutableLiveData<String> = MutableLiveData()
    private val words: MutableLiveData<Set<Word>> = MutableLiveData()

    private val nameIsValid: MutableLiveData<Boolean> = MutableLiveData()
    private val wordsAreValid: MutableLiveData<Boolean> = MutableLiveData()

    private val successfullySaved = MutableLiveData<Boolean>()

    fun isDataLoaded(): LiveData<Boolean> = dataLoaded

    fun getStudyList(): LiveData<StudyList> = studyList
    fun getWords(): LiveData<Set<Word>> = words

    fun isNameValid(): LiveData<Boolean> = nameIsValid
    fun isWordsValid(): LiveData<Boolean> = wordsAreValid

    fun isSuccessfullySaved(): LiveData<Boolean> = successfullySaved

    fun load(id: Long?) {
        if (id != null) {
            viewModelScope.launch {
                studyListRepository.get(id).collect {
                    studyList.postValue(it)
                    name.postValue(it.name)
                    words.postValue(it.words)

                    dataLoaded.postValue(true)
                }
            }
        } else {
            dataLoaded.value = true
        }
    }

    fun getSelectedWordIds(): LongArray = words.value?.map { it.id!! }?.toLongArray() ?: LongArray(0)

    fun wordsSelected(wordIds: LongArray) {
        viewModelScope.launch {
            wordRepository.get(wordIds.toSet()).collect {
                words.postValue(it.toSet())
            }
        }
    }

    fun editName(newValue: String) {
        name.value = newValue
    }

    private fun validateName(): Boolean = (name.value?.run { length >= 3 } ?: false).also {
        nameIsValid.value = it
    }

    private fun validateWords(): Boolean = (words.value?.run { size > 0 } ?: false).also {
        wordsAreValid.value = it
    }

    fun save() {
        if (validateName() && validateWords()) {
            val entity = studyList.value?.copy(
                name = name.value!!,
                words = words.value!!
            ) ?: StudyList(
                name = name.value!!,
                words = words.value!!
            )
            viewModelScope.launch(Dispatchers.IO) {
                studyListRepository.save(entity)
                successfullySaved.postValue(true)
            }
        } else {
            successfullySaved.value = false
        }
    }
}
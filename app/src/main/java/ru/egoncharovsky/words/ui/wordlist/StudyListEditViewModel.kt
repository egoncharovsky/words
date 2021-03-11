package ru.egoncharovsky.words.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.egoncharovsky.words.domain.DictionaryEntry
import ru.egoncharovsky.words.domain.StudyList
import ru.egoncharovsky.words.repository.DictionaryEntryRepository
import ru.egoncharovsky.words.repository.StudyListRepository

class StudyListEditViewModel : ViewModel() {

    private val studyList: MutableLiveData<StudyList> = MutableLiveData()

    private val name: MutableLiveData<String> = MutableLiveData()
    private val dictionaryEntries: MutableLiveData<Set<DictionaryEntry>> = MutableLiveData()

    private val nameIsValid: MutableLiveData<Boolean> = MutableLiveData()
    private val dictionaryEntriesAreValid: MutableLiveData<Boolean> = MutableLiveData()

    fun getDictionaryEntries(): LiveData<Set<DictionaryEntry>> = dictionaryEntries

    fun isNameValid(): LiveData<Boolean> = nameIsValid
    fun areDictionaryEntriesValid(): LiveData<Boolean> = dictionaryEntriesAreValid

    fun load(id: Long): StudyList {
        return if (this.studyList.value == null) {
            val studyList = StudyListRepository.get(id).also {
                this.studyList.value = it
            }

            dictionaryEntries.value = studyList.dictionaryEntries
            name.value = studyList.name

            studyList
        } else studyList.value!!
    }

    fun dictionaryEntriesSelected(dictionaryEntryIds: LongArray) {
        this.dictionaryEntries.value = dictionaryEntryIds.map { DictionaryEntryRepository.get(it) }.toSet()
        validateDictionaryEntries()
    }

    fun editName(newValue: String) {
        name.value = newValue
        validateName()
    }

    private fun validateName(): Boolean = (name.value?.run { length >= 3 } ?: false).also {
        nameIsValid.value = it
    }

    private fun validateDictionaryEntries(): Boolean = (dictionaryEntries.value?.run { size > 0 } ?: false).also {
        dictionaryEntriesAreValid.value = it
    }

    fun save(): Boolean = if (validateName() && validateDictionaryEntries()) {
        val entity = studyList.value?.also {
            it.name = name.value!!
            it.dictionaryEntries = dictionaryEntries.value!!
        } ?: StudyList(null, name.value!!, dictionaryEntries.value!!)

        StudyListRepository.save(entity)
        true
    } else false
}
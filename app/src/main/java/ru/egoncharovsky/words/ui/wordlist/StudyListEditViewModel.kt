package ru.egoncharovsky.words.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mu.KotlinLogging
import ru.egoncharovsky.words.domain.StudyList
import ru.egoncharovsky.words.domain.Word
import ru.egoncharovsky.words.repository.DictionaryEntryRepository
import ru.egoncharovsky.words.repository.StudyListRepository

class StudyListEditViewModel : ViewModel() {

    private val logger = KotlinLogging.logger {}

    private val studyList: MutableLiveData<StudyList> = MutableLiveData()

    private val name: MutableLiveData<String> = MutableLiveData()
    private val words: MutableLiveData<Set<Word>> = MutableLiveData()

    private val nameIsValid: MutableLiveData<Boolean> = MutableLiveData()
    private val wordsAreValid: MutableLiveData<Boolean> = MutableLiveData()

    fun getWords(): LiveData<Set<Word>> = words
    fun isNameValid(): LiveData<Boolean> = nameIsValid

    fun areWordsValid(): LiveData<Boolean> = wordsAreValid

    fun load(id: Long): StudyList {
        return if (this.studyList.value == null) {
            val studyList = StudyListRepository.get(id).also {
                this.studyList.value = it
            }

            words.value = studyList.words
            name.value = studyList.name

            studyList
        } else studyList.value!!
    }

    fun getDictionaryEntryIds(): LongArray {
        return words.value?.mapNotNull { word ->
            DictionaryEntryRepository.getByWord(word)?.id.also {
                if (it == null) logger.error("No id found for word $word")
            }
        }?.toLongArray() ?: LongArray(0)
    }

    fun dictionaryEntriesSelected(dictionaryEntryIds: LongArray) {
        this.words.value = dictionaryEntryIds.map { DictionaryEntryRepository.get(it).word }.toSet()
        validateWords()
    }

    fun editName(newValue: String) {
        name.value = newValue
        validateName()
    }

    private fun validateName(): Boolean = (name.value?.run { length >= 3 } ?: false).also {
        nameIsValid.value = it
    }

    private fun validateWords(): Boolean = (words.value?.run { size > 0 } ?: false).also {
        wordsAreValid.value = it
    }

    fun save(): Boolean = if (validateName() && validateWords()) {
        val entity = studyList.value?.copy(
            name = name.value!!,
            words = words.value!!
        ) ?: StudyList(
            name = name.value!!,
            words = words.value!!
        )

        StudyListRepository.save(entity)
        true
    } else false
}
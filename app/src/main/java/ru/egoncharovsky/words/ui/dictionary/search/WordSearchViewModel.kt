package ru.egoncharovsky.words.ui.dictionary.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.repository.persistent.WordRepository

abstract class WordSearchViewModel(
    private val wordRepository: WordRepository
) : ViewModel(), WordSearchableViewModel {

    private var request: Job? = null

    private val sort = MutableLiveData<SortType>().apply {
        value = SortType.DEFAULT
    }
    private val words: MutableLiveData<List<Word>> = MutableLiveData<List<Word>>()
    private val wordsPopularity: MutableLiveData<Map<Long, Int>> = MutableLiveData()

    fun onInit() {
        loadPopularityStatistic()
        request(getAllRequest())
    }

    override fun setSort(sortType: SortType) {
        this.sort.value = sortType
    }

    override fun search(newText: String?) {
        newText?.trim()?.let { value ->
            if (value.isNotBlank()) {
                request(searchWordRequest(value))
            } else {
                cancelSearch()
            }
        }
    }

    override fun cancelSearch() {
        request(getAllRequest())
    }

    override fun onSortChanged() {
        words.value?.let {
            words.value = sorted(it)
        }
    }

    override fun getSort(): LiveData<SortType> = sort

    override fun getWords(): LiveData<List<Word>> = words

    override fun getWordsPopularity(): LiveData<Map<Long, Int>> = wordsPopularity

    fun getPopularityOf(id: Long): Int? = wordsPopularity.value?.get(id)

    abstract fun getAllRequest(): Flow<List<Word>>
    abstract fun searchWordRequest(value: String): Flow<List<Word>>

    private fun loadPopularityStatistic() {
        viewModelScope.launch {
            wordRepository.getPopularityRatings().collect {
                wordsPopularity.postValue(it)
            }
        }
    }

    private fun request(flow: Flow<List<Word>>) {
        request?.cancel()
        request = viewModelScope.launch {
            flow.map { sorted(it) }.collect {
                words.postValue(it)
            }
        }
    }

    private fun sorted(list: List<Word>): List<Word> {
        return when (sort.value!!) {
            SortType.DEFAULT ->
                list.sortedBy { it.id }
            SortType.WORD_VALUE_ASK ->
                list.sortedBy { it.value.lowercase() }
            SortType.WORD_VALUE_DESC ->
                list.sortedByDescending { it.value.lowercase() }
            SortType.WORD_UPLOAD_DATE_ASK ->
                list.sortedWith(compareBy<Word> { it.createdAt }.thenBy { it.id })
            SortType.WORD_UPLOAD_DATE_DESC ->
                list.sortedWith(compareByDescending<Word> { it.createdAt }.thenByDescending { it.id })
            SortType.WORD_POPULARITY_ASK ->
                list.sortedWith(compareBy { wordsPopularity.value!![it.id!!] })
            SortType.WORD_POPULARITY_DESC ->
                list.sortedWith(compareByDescending { wordsPopularity.value!![it.id!!] })
        }
    }

    enum class SortType {
        DEFAULT,
        WORD_VALUE_ASK,
        WORD_VALUE_DESC,
        WORD_UPLOAD_DATE_ASK,
        WORD_UPLOAD_DATE_DESC,
        WORD_POPULARITY_DESC,
        WORD_POPULARITY_ASK;
    }
}
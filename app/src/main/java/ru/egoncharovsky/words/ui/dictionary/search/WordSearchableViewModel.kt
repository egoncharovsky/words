package ru.egoncharovsky.words.ui.dictionary.search

import androidx.lifecycle.LiveData
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.ui.dictionary.search.WordSearchViewModel.SortType

interface WordSearchableViewModel {
    fun setSort(sortType: SortType)
    fun search(newText: String?)
    fun cancelSearch()
    fun onSortChanged()

    fun getSort(): LiveData<SortType>
    fun getWords(): LiveData<List<Word>>
}
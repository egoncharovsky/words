package ru.egoncharovsky.words.ui.dictionary.search

import androidx.lifecycle.LiveData
import ru.egoncharovsky.words.domain.entity.Word

interface WordSearchableViewModel {
    fun setSort(sortType: WordSearchWidget.SortType)
    fun search(newText: String?)
    fun cancelSearch()
    fun onSortChanged()

    fun getSort(): LiveData<WordSearchWidget.SortType>
    fun getWords(): LiveData<List<Word>>
}
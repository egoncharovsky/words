package ru.egoncharovsky.words.ui.dictionary.search

import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.ui.dictionary.search.WordSearchViewModel.SortType
import ru.egoncharovsky.words.ui.items

class WordSearchWidget(
    private val sortButton: ImageButton,
    private val searchView: SearchView,

    val model: WordSearchableViewModel,
    val fragment: Fragment,
) {
    private lateinit var sortMenu: PopupMenu

    fun onViewCreated(view: View, resultsObserver: Observer<List<Word>>) {
        sortMenu = configureSortingMenu(view.context, sortButton)
        configureSearchInput(searchView)
        configureObservers(fragment.viewLifecycleOwner, resultsObserver)
    }

    private fun configureSortingMenu(context: Context, button: ImageButton): PopupMenu {
        val popup = PopupMenu(context, button).apply {
            SortType.values().forEach { sort ->
                menu.add(sortTypeTitle(sort)).apply { isCheckable = true }
            }
            menu.setGroupCheckable(0, true, true)

            setOnMenuItemClickListener { item ->
                val sortType = SortType.values().find {
                    sortTypeTitle(it) == item.title
                } ?: throw EnumConstantNotPresentException(
                    SortType::class.java,
                    "for title ${item.title}"
                )
                model.setSort(sortType)
                true
            }
        }
        button.setOnClickListener {
            popup.show()
        }
        return popup
    }

    private fun configureSearchInput(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                model.search(newText)
                return true
            }
        })
        searchView.setOnCloseListener {
            model.cancelSearch()
            true
        }
        searchView.setOnClickListener {
            searchView.isIconified = false
        }
    }

    private fun setCheckedSortType(sortMenu: PopupMenu, sort: SortType) {
        val sortTypeTitle = sortTypeTitle(sort)
        sortMenu.menu.items().find { it.title == sortTypeTitle }?.isChecked = true
    }

    private fun sortTypeTitle(sortType: SortType): String = when (sortType) {
        SortType.DEFAULT -> fragment.getString(R.string.default_value)
        SortType.WORD_VALUE_ASK -> fragment.getString(R.string.word_value_ask)
        SortType.WORD_VALUE_DESC -> fragment.getString(R.string.word_value_desc)
        SortType.WORD_UPLOAD_DATE_ASK -> fragment.getString(R.string.word_uploaded_asc)
        SortType.WORD_UPLOAD_DATE_DESC -> fragment.getString(R.string.word_uploaded_desc)
    }

    private fun configureObservers(lifecycleOwner: LifecycleOwner, resultsObserver: Observer<List<Word>>) {
        // search results
        model.getWords().observe(lifecycleOwner, resultsObserver)
        // apply sorting
        model.getSort().observe(lifecycleOwner) { sort ->
            model.onSortChanged()
            setCheckedSortType(sortMenu, sort)
        }
    }
}
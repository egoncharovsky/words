package ru.egoncharovsky.words.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dictionary.*
import kotlinx.android.synthetic.main.fragment_dictionary_item.view.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.DictionaryEntry
import ru.egoncharovsky.words.ui.RecyclerViewAdapter
import ru.egoncharovsky.words.ui.items
import ru.egoncharovsky.words.ui.observe

class DictionaryFragment : Fragment() {

    private lateinit var dictionaryViewModel: DictionaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dictionaryViewModel = ViewModelProvider(this).get(DictionaryViewModel::class.java)

        return inflater.inflate(R.layout.fragment_dictionary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val popup = PopupMenu(view.context, sortButton).apply {

            DictionaryViewModel.SortType.values().forEach { sort ->
                menu.add(sortTypeTitle(sort)).apply { isCheckable = true }
            }
            menu.setGroupCheckable(0, true, true)

            setOnMenuItemClickListener { item ->
                val sortType = DictionaryViewModel.SortType.values().find {
                    sortTypeTitle(it) == item.title
                } ?: throw EnumConstantNotPresentException(
                    DictionaryViewModel.SortType::class.java,
                    "for title ${item.title}"
                )
                dictionaryViewModel.setSort(sortType)
                true
            }
        }
        sortButton.setOnClickListener {
            popup.show()
        }
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                dictionaryViewModel.search(newText)
                return true
            }
        })
        search.setOnCloseListener {
            dictionaryViewModel.cancelSearch()
            true
        }

        observe(dictionaryViewModel.getDictionaryEntries()) {
            dictionaryList.layoutManager = LinearLayoutManager(view.context)
            dictionaryList.adapter = DictionaryEntryAdapter(it)
        }
        observe(dictionaryViewModel.getSort()) { sort ->
            dictionaryViewModel.onSortChanged()
            val sortTypeTitle = sortTypeTitle(sort)
            popup.menu.items().find { it.title == sortTypeTitle }?.isChecked = true
        }
    }

    private fun sortTypeTitle(sortType: DictionaryViewModel.SortType): String = when (sortType) {
        DictionaryViewModel.SortType.DEFAULT -> getString(R.string.default_value)
        DictionaryViewModel.SortType.WORD_VALUE_ASK -> getString(R.string.word_value_ask)
        DictionaryViewModel.SortType.WORD_VALUE_DESC -> getString(R.string.word_value_desc)
    }

    class DictionaryEntryAdapter(values: List<DictionaryEntry>) : RecyclerViewAdapter<DictionaryEntry>(values) {
        override val itemLayoutId: Int = R.layout.fragment_dictionary_item

        override fun bind(itemView: View, item: DictionaryEntry) {
            itemView.wordValue.text = item.word.value
            itemView.wordTranslation.text = item.word.translation
        }
    }
}
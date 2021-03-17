package ru.egoncharovsky.words.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentDictionaryBinding
import ru.egoncharovsky.words.databinding.FragmentDictionaryItemBinding
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.ui.RecyclerViewAdapter
import ru.egoncharovsky.words.ui.items
import ru.egoncharovsky.words.ui.observe

@AndroidEntryPoint
open class DictionaryFragment : Fragment() {

    protected val binding: FragmentDictionaryBinding by viewBinding()
    protected lateinit var dictionaryViewModel: DictionaryViewModel
    protected lateinit var dictionaryAdapter: RecyclerViewAdapter<Word, *>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dictionaryViewModel = ViewModelProvider(this).get(DictionaryViewModel::class.java)
        dictionaryAdapter = WordAdapter()

        return inflater.inflate(R.layout.fragment_dictionary, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.dictionaryList.layoutManager = LinearLayoutManager(view.context)
        binding.dictionaryList.adapter = dictionaryAdapter

        val popup = PopupMenu(view.context, binding.sortButton).apply {

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
        binding.sortButton.setOnClickListener {
            popup.show()
        }
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                dictionaryViewModel.search(newText)
                return true
            }
        })
        binding.search.setOnCloseListener {
            dictionaryViewModel.cancelSearch()
            true
        }
        binding.search.setOnClickListener {
            binding.search.isIconified = false
        }

        observe(dictionaryViewModel.getWords()) {
            dictionaryAdapter.update(it)
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

    class WordAdapter : RecyclerViewAdapter<Word, FragmentDictionaryItemBinding>() {
        override val bindingInflate: (inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> FragmentDictionaryItemBinding =
            FragmentDictionaryItemBinding::inflate

        override fun bind(binding: FragmentDictionaryItemBinding, item: Word) {
            binding.wordValue.text = item.value
            binding.wordTranslation.text = item.translation
        }
    }
}
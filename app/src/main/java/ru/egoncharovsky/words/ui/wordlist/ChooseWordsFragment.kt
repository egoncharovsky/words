package ru.egoncharovsky.words.ui.wordlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentChooseWordsBinding
import ru.egoncharovsky.words.databinding.FragmentChooseWordsItemBinding
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.ui.SelectableRecyclerViewAdapter
import ru.egoncharovsky.words.ui.dictionary.search.WordSearchWidget
import ru.egoncharovsky.words.ui.navigateUp
import ru.egoncharovsky.words.ui.setNavigationResult

@AndroidEntryPoint
class ChooseWordsFragment : Fragment() {

    private lateinit var binding: FragmentChooseWordsBinding
    private val args: ChooseWordsFragmentArgs by navArgs()

    private val viewModel: ChooseWordsViewModel by viewModels()
    private lateinit var adapter: SelectableRecyclerViewAdapter<Word, Long, FragmentChooseWordsItemBinding>
    private lateinit var tracker: SelectionTracker<Long>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        adapter = ChooseWordsAdapter(requireContext(), viewModel::isIncludedInAnotherList)

        binding = FragmentChooseWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.dictionaryList.layoutManager = LinearLayoutManager(view.context)
        binding.dictionaryList.adapter = adapter

        WordSearchWidget(binding.sortButton, binding.search, viewModel, this)
            .onViewCreated(view) {
                adapter.update(it)
            }

        configureSelections(savedInstanceState)

        binding.showAlreadyIncluded.setOnCheckedChangeListener { _, isChecked ->
            viewModel.searchFiltersUpdated(isChecked, binding.search.query.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker.onSaveInstanceState(outState)
    }

    private fun configureSelections(savedInstanceState: Bundle?) {
        tracker = adapter.observe(binding.dictionaryList) {
            viewModel.selectionUpdated(it)
        }
        args.chosen?.let {
            tracker.setItemsSelected(it.toList(), true)
        }

        binding.accept.setOnClickListener {
            viewModel.getChosenDictionaryIds().value?.let {
                setNavigationResult(it.toLongArray())
            }
            navigateUp()
        }

        tracker.onRestoreInstanceState(savedInstanceState)
    }

    class ChooseWordsAdapter(
        val context: Context,
        val highlightBackground: (Long) -> Boolean
        ) :
        SelectableRecyclerViewAdapter<Word, Long, FragmentChooseWordsItemBinding>(StorageStrategy.createLongStorage()) {
        override val bindingInflate: (inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> FragmentChooseWordsItemBinding =
            FragmentChooseWordsItemBinding::inflate

        override fun bind(binding: FragmentChooseWordsItemBinding, item: Word, isActivated: Boolean) {
            if (highlightBackground(item.id!!)) {
                binding.chooseWordItemLayout.background =
                    ContextCompat.getDrawable(context, R.drawable.item_background_highlight)
            }

            binding.wordValue.text = item.value
            binding.wordTranslation.text = item.translation
            binding.selectedCheckBox.isChecked = isActivated
        }

        override fun getIdentifier(item: Word): Long = item.id!!
    }
}
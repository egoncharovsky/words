package ru.egoncharovsky.words.ui.wordlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentChooseWordsBinding
import ru.egoncharovsky.words.databinding.FragmentChooseWordsItemBinding
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.ui.SelectableRecyclerViewAdapter
import ru.egoncharovsky.words.ui.dictionary.DictionaryFragment
import ru.egoncharovsky.words.ui.navigateUp
import ru.egoncharovsky.words.ui.setNavigationResult

class ChooseWordsFragment : DictionaryFragment() {

    private val chooseBinding: FragmentChooseWordsBinding by viewBinding()
    private val args: ChooseWordsFragmentArgs by navArgs()

    private lateinit var chooseWordsViewModel: ChooseWordsViewModel
    private lateinit var selectableAdapter: SelectableRecyclerViewAdapter<Word, Long, FragmentChooseWordsItemBinding>
    private lateinit var tracker: SelectionTracker<Long>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        chooseWordsViewModel = ViewModelProvider(this).get(ChooseWordsViewModel::class.java).also {
            dictionaryViewModel = it
        }
        selectableAdapter = ChooseWordsAdapter().also {
            dictionaryAdapter = it
        }

        return inflater.inflate(R.layout.fragment_choose_words, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracker = selectableAdapter.observe(binding.dictionaryList) {
            chooseWordsViewModel.selectionUpdated(it)
        }
        args.chosen?.let {
            tracker.setItemsSelected(it.toList(), true)
        }

        chooseBinding.accept.setOnClickListener {
            chooseWordsViewModel.getChosenDictionaryIds().value?.let {
                setNavigationResult(it.toLongArray())
            }
            navigateUp()
        }

        tracker.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker.onSaveInstanceState(outState)
    }

    class ChooseWordsAdapter :
        SelectableRecyclerViewAdapter<Word, Long, FragmentChooseWordsItemBinding>(StorageStrategy.createLongStorage()) {
        override val bindingInflate: (inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> FragmentChooseWordsItemBinding =
            FragmentChooseWordsItemBinding::inflate

        override fun bind(binding: FragmentChooseWordsItemBinding, item: Word, isActivated: Boolean) {
            binding.wordValue.text = item.value
            binding.wordTranslation.text = item.translation
            binding.selectedCheckBox.isChecked = isActivated
        }

        override fun getIdentifier(item: Word): Long = item.id!!
    }
}
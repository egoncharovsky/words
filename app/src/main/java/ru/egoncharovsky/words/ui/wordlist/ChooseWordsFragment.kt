package ru.egoncharovsky.words.ui.wordlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.StorageStrategy
import kotlinx.android.synthetic.main.fragment_choose_words.*
import kotlinx.android.synthetic.main.fragment_choose_words_item.view.*
import kotlinx.android.synthetic.main.fragment_dictionary.*
import kotlinx.android.synthetic.main.fragment_dictionary_item.view.wordTranslation
import kotlinx.android.synthetic.main.fragment_dictionary_item.view.wordValue
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.DictionaryEntry
import ru.egoncharovsky.words.ui.SelectableRecyclerViewAdapter
import ru.egoncharovsky.words.ui.dictionary.DictionaryFragment
import ru.egoncharovsky.words.ui.setNavigationResult

class ChooseWordsFragment : DictionaryFragment() {

    private val args: ChooseWordsFragmentArgs by navArgs()

    private lateinit var chooseWordsViewModel: ChooseWordsViewModel
    private lateinit var selectableAdapter: SelectableRecyclerViewAdapter<DictionaryEntry, Long>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        chooseWordsViewModel = ViewModelProvider(this).get(ChooseWordsViewModel::class.java).also {
            dictionaryViewModel = it
        }
        selectableAdapter = ChooseWordsAdapter().also {
            adapter = it
        }

        return inflater.inflate(R.layout.fragment_choose_words, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectableAdapter.observe(dictionaryList) {
            chooseWordsViewModel.selectionUpdated(it)
        }

        args.chosen?.let {
            selectableAdapter.tracker!!.setItemsSelected(it.toList(), true)
        }

        accept.setOnClickListener {
            chooseWordsViewModel.getChosenDictionaryIds().value?.let {
                setNavigationResult(it.toLongArray())
            }
            findNavController().navigateUp()
        }

        selectableAdapter.tracker?.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectableAdapter.tracker?.onSaveInstanceState(outState)
    }

    class ChooseWordsAdapter : SelectableRecyclerViewAdapter<DictionaryEntry, Long>(StorageStrategy.createLongStorage()) {
        override val itemLayoutId: Int = R.layout.fragment_choose_words_item

        override fun bind(itemView: View, item: DictionaryEntry, isActivated: Boolean) {
            itemView.wordValue.text = item.word.value
            itemView.wordTranslation.text = item.word.translation
            itemView.selectedCheckBox.isChecked = isActivated
        }

        override fun getIdentifier(item: DictionaryEntry): Long = item.id!!

    }
}
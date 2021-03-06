package ru.egoncharovsky.words.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dictionary.*
import kotlinx.android.synthetic.main.fragment_dictionary_item.view.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.DictionaryEntry
import ru.egoncharovsky.words.ui.RecyclerViewAdapter
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
        observe(dictionaryViewModel.getDictionaryEntries()) {
            dictionaryList.layoutManager = LinearLayoutManager(view.context)
            dictionaryList.adapter = DictionaryEntryAdapter(it)
        }
    }

    class DictionaryEntryAdapter(values: List<DictionaryEntry>) : RecyclerViewAdapter<DictionaryEntry>(values) {
        override val itemLayoutId: Int = R.layout.fragment_dictionary_item

        override fun bind(itemView: View, item: DictionaryEntry) {
            itemView.wordValue.text = item.word.value
            itemView.wordTranslation.text = item.word.translation
        }
    }
}
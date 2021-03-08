package ru.egoncharovsky.words.ui.wordlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_word_lists.*
import kotlinx.android.synthetic.main.fragment_word_lists_item.view.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.WordList
import ru.egoncharovsky.words.ui.RecyclerViewAdapter
import ru.egoncharovsky.words.ui.observe

class WordListsFragment : Fragment() {

    private lateinit var wordListsViewModel: WordListsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        wordListsViewModel = ViewModelProvider(this).get(WordListsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_word_lists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(wordListsViewModel.getWordLists()) {
            wordLists.layoutManager = LinearLayoutManager(view.context)
            wordLists.adapter = WordListAdapter(it)
        }

        addList.setOnClickListener {
            findNavController().navigate(
                WordListsFragmentDirections.actionNavWordListsToWordListEditFragment()
            )
        }
    }

    inner class WordListAdapter(values: List<WordList>) : RecyclerViewAdapter<WordList>(values) {
        override val itemLayoutId: Int = R.layout.fragment_word_lists_item

        override fun bind(itemView: View, item: WordList) {
            itemView.name.text = item.name
            itemView.count.text = String.format(getString(R.string.words_count), item.words.size)
            itemView.editList.setOnClickListener {
                findNavController().navigate(
                    WordListsFragmentDirections.actionNavWordListsToWordListEditFragment(item.id.toString())
                )
            }
        }

    }
}
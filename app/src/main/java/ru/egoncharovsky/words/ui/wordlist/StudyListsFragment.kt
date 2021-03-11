package ru.egoncharovsky.words.ui.wordlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_study_lists.*
import kotlinx.android.synthetic.main.fragment_study_lists_item.view.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.StudyList
import ru.egoncharovsky.words.ui.NavArgLongNullable
import ru.egoncharovsky.words.ui.RecyclerViewAdapter
import ru.egoncharovsky.words.ui.observe

class StudyListsFragment : Fragment() {

    private lateinit var studyListsViewModel: StudyListsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        studyListsViewModel = ViewModelProvider(this).get(StudyListsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_study_lists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(studyListsViewModel.getWordLists()) {
            wordLists.layoutManager = LinearLayoutManager(view.context)
            wordLists.adapter = WordListAdapter(it)
        }

        addList.setOnClickListener {
            findNavController().navigate(
                StudyListsFragmentDirections.editWordList()
            )
        }

        studyListsViewModel.load()
    }

    inner class WordListAdapter(values: List<StudyList>) : RecyclerViewAdapter<StudyList>(values) {
        override val itemLayoutId: Int = R.layout.fragment_study_lists_item

        override fun bind(itemView: View, item: StudyList) {
            itemView.name.text = item.name
            itemView.count.text = String.format(getString(R.string.words_count), item.dictionaryEntries.size)
            itemView.editList.setOnClickListener {
                findNavController().navigate(
                    StudyListsFragmentDirections.editWordList(NavArgLongNullable(item.id!!))
                )
            }
        }

    }
}
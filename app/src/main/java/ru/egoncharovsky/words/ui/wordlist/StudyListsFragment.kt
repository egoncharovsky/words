package ru.egoncharovsky.words.ui.wordlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_study_lists.*
import kotlinx.android.synthetic.main.fragment_study_lists_item.view.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.entity.StudyList
import ru.egoncharovsky.words.ui.NavArgLongNullable
import ru.egoncharovsky.words.ui.RecyclerViewAdapter
import ru.egoncharovsky.words.ui.observe

@AndroidEntryPoint
class StudyListsFragment : Fragment() {

    private val studyListsViewModel: StudyListsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_study_lists, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(studyListsViewModel.studyLists) {
            wordLists.layoutManager = LinearLayoutManager(view.context)
            wordLists.adapter = WordListAdapter(it)
        }

        addList.setOnClickListener {
            findNavController().navigate(
                StudyListsFragmentDirections.editWordList()
            )
        }
    }

    inner class WordListAdapter(values: List<StudyList>) : RecyclerViewAdapter<StudyList>(values) {
        override val itemLayoutId: Int = R.layout.fragment_study_lists_item

        override fun bind(itemView: View, item: StudyList) {
            itemView.name.text = item.name
            itemView.count.text = String.format(getString(R.string.words_count), item.words.size)
            itemView.editList.setOnClickListener {
                findNavController().navigate(
                    StudyListsFragmentDirections.editWordList(NavArgLongNullable(item.id!!))
                )
            }
            itemView.study.setOnClickListener {
                findNavController().navigate(
                    StudyListsFragmentDirections.startQuiz(item.id!!)
                )
            }
        }

    }
}
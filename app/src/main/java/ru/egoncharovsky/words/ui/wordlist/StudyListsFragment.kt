package ru.egoncharovsky.words.ui.wordlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentStudyListsBinding
import ru.egoncharovsky.words.databinding.FragmentStudyListsItemBinding
import ru.egoncharovsky.words.domain.entity.StudyList
import ru.egoncharovsky.words.ui.NavArgLong
import ru.egoncharovsky.words.ui.RecyclerViewAdapter
import ru.egoncharovsky.words.ui.observe

@AndroidEntryPoint
class StudyListsFragment : Fragment() {

    private val binding: FragmentStudyListsBinding by viewBinding()
    private val studyListsViewModel: StudyListsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_study_lists, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(studyListsViewModel.studyLists) {
            binding.wordLists.layoutManager = LinearLayoutManager(view.context)
            binding.wordLists.adapter = WordListAdapter(it)
        }

        binding.addList.setOnClickListener {
            findNavController().navigate(
                StudyListsFragmentDirections.editWordList()
            )
        }
    }

    inner class WordListAdapter(values: List<StudyList>) :
        RecyclerViewAdapter<StudyList, FragmentStudyListsItemBinding>(values) {
        override val bindingInflate: (inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> FragmentStudyListsItemBinding =
            FragmentStudyListsItemBinding::inflate

        override fun bind(binding: FragmentStudyListsItemBinding, item: StudyList) {
            binding.name.text = item.name
            binding.count.text = String.format(getString(R.string.words_count), item.words.size)
            binding.editList.setOnClickListener {
                findNavController().navigate(
                    StudyListsFragmentDirections.editWordList(NavArgLong(item.id!!))
                )
            }
            binding.study.setOnClickListener {
                findNavController().navigate(
                    StudyListsFragmentDirections.startQuiz(item.id!!)
                )
            }
        }
    }
}
package ru.egoncharovsky.words.ui.wordlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentDictionaryItemBinding
import ru.egoncharovsky.words.databinding.FragmentStudyListEditBinding
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.ui.RecyclerViewAdapter
import ru.egoncharovsky.words.ui.dictionary.WordAdapter
import ru.egoncharovsky.words.ui.navigate
import ru.egoncharovsky.words.ui.navigateUp
import ru.egoncharovsky.words.ui.observe
import ru.egoncharovsky.words.ui.observeNavigationResult

@AndroidEntryPoint
class StudyListEditFragment : Fragment() {

    private lateinit var binding: FragmentStudyListEditBinding
    private val studyListEditViewModel: StudyListEditViewModel by viewModels()
    private lateinit var adapter: RecyclerViewAdapter<Word, FragmentDictionaryItemBinding>
    private val args: StudyListEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = WordAdapter()

        binding = FragmentStudyListEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        args.wordListId?.let {
            val wordId = it.value
            studyListEditViewModel.load(wordId)

            binding.delete.visibility = View.VISIBLE
            binding.delete.setOnClickListener {
                studyListEditViewModel.delete(wordId)
            }
        } ?: kotlin.run {
            studyListEditViewModel.new()

            binding.delete.visibility = View.INVISIBLE
        }

        binding.words.layoutManager = LinearLayoutManager(view.context)
        binding.words.adapter = adapter

        binding.count.text = String.format(getString(R.string.words_count), 0)

        observe(studyListEditViewModel.isDataLoaded()) {
            binding.choose.isActivated = it
            binding.name.isActivated = it
        }

        observeNavigationResult<LongArray> {
            studyListEditViewModel.wordsSelected(it)
        }

        observe(studyListEditViewModel.getStudyList()) {
            binding.name.setText(it.name, TextView.BufferType.EDITABLE)
        }
        observe(studyListEditViewModel.getWords()) {
            binding.count.text = String.format(getString(R.string.words_count), it.size)
            adapter.update(it.toList())
        }

        observe(studyListEditViewModel.isNameValid()) {
            binding.name.error = when (it) {
                false -> getString(R.string.should_have_at_least_3_letters)
                true -> null
            }
        }
        observe(studyListEditViewModel.isWordsValid()) {
            binding.count.error = when (it) {
                false -> getString(R.string.should_have_at_least_1)
                true -> null
            }
        }

        binding.choose.setOnClickListener {
            navigate(StudyListEditFragmentDirections.chooseWords(studyListEditViewModel.getSelectedWordIds()))
        }
        binding.name.addTextChangedListener {
            studyListEditViewModel.editName(it.toString())
        }
        binding.save.setOnClickListener {
            studyListEditViewModel.save()
        }
        observe(studyListEditViewModel.isFinished()) { successfully ->
            if (successfully) {
                navigateUp()
            }
        }
    }
}
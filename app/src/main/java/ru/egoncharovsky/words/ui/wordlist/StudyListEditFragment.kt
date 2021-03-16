package ru.egoncharovsky.words.ui.wordlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dictionary_item.view.*
import kotlinx.android.synthetic.main.fragment_study_list_edit.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.ui.RecyclerViewAdapter
import ru.egoncharovsky.words.ui.observe
import ru.egoncharovsky.words.ui.observeNavigationResult

@AndroidEntryPoint
class StudyListEditFragment : Fragment() {

    private val studyListEditViewModel: StudyListEditViewModel by viewModels()
    private lateinit var adapter: RecyclerViewAdapter<Word>
    private val args: StudyListEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = StudyListWordsAdapter()

        return inflater.inflate(R.layout.fragment_study_list_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        studyListEditViewModel.load(args.wordListId?.value)

        words.layoutManager = LinearLayoutManager(view.context)
        words.adapter = adapter

        count.text = String.format(getString(R.string.words_count), 0)

        observe(studyListEditViewModel.isDataLoaded()) {
            choose.isActivated = it
            name.isActivated = it
        }

        observeNavigationResult<LongArray> {
            studyListEditViewModel.dictionaryEntriesSelected(it)
        }

        observe(studyListEditViewModel.getStudyList()) {
            name.setText(it.name, TextView.BufferType.EDITABLE)
        }
        observe(studyListEditViewModel.getWords()) {
            count.text = String.format(getString(R.string.words_count), it.size)
            adapter.update(it.toList())
        }

        observe(studyListEditViewModel.isNameValid()) {
            name.error = when (it) {
                false -> getString(R.string.should_have_at_least_3_letters)
                true -> null
            }
        }
        observe(studyListEditViewModel.isWordsValid()) {
            count.error = when (it) {
                false -> getString(R.string.should_have_at_least_1)
                true -> null
            }
        }

        choose.setOnClickListener {
            findNavController().navigate(
                StudyListEditFragmentDirections.chooseWords(studyListEditViewModel.getSelectedDictionaryEntryIds())
            )
        }
        name.addTextChangedListener {
            studyListEditViewModel.editName(it.toString())
        }
        save.setOnClickListener {
            studyListEditViewModel.save()
        }
        observe(studyListEditViewModel.isSuccessfullySaved()) { successfully ->
            if (successfully) {
                findNavController().navigateUp()
            }
        }
    }

    inner class StudyListWordsAdapter : RecyclerViewAdapter<Word>() {
        override val itemLayoutId: Int = R.layout.fragment_study_list_item

        override fun bind(itemView: View, item: Word) {
            itemView.wordValue.text = item.value
            itemView.wordTranslation.text = item.translation
        }

    }
}
package ru.egoncharovsky.words.ui.wordlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dictionary_item.view.*
import kotlinx.android.synthetic.main.fragment_study_list_edit.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.Word
import ru.egoncharovsky.words.ui.RecyclerViewAdapter
import ru.egoncharovsky.words.ui.observe
import ru.egoncharovsky.words.ui.observeNavigationResult

class StudyListEditFragment : Fragment() {

    private lateinit var studyListEditViewModel: StudyListEditViewModel
    private val args: StudyListEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        studyListEditViewModel = ViewModelProvider(this).get(StudyListEditViewModel::class.java)

        return inflater.inflate(R.layout.fragment_study_list_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        args.wordListId?.let {
            studyListEditViewModel.load(it.value)
        }

        observeNavigationResult<LongArray> {
            studyListEditViewModel.wordsSelected(it)
        }

        observe(studyListEditViewModel.getWords()) {
            count.text = String.format(getString(R.string.words_count), it.size)

            words.layoutManager = LinearLayoutManager(view.context)
            words.adapter = WordAdapter(it)
        }
        choose.setOnClickListener {
            findNavController().navigate(
                StudyListEditFragmentDirections.chooseWords(studyListEditViewModel.getDictionaryEntryIds().value)
            )
        }
    }

    inner class WordAdapter(values: List<Word>) : RecyclerViewAdapter<Word>(values) {
        override val itemLayoutId: Int = R.layout.fragment_study_list_item

        override fun bind(itemView: View, item: Word) {
            itemView.wordValue.text = item.value
            itemView.wordTranslation.text = item.translation
        }

    }
}
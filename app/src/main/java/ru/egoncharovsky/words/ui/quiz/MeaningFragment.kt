package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.fragment_quiz_meaning.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.ui.observe

class MeaningFragment : Fragment() {

    private val quizViewModel: QuizViewModel by activityViewModels()

    private lateinit var meaningWithShowedTrigger: LiveData<QuizViewModel.MeaningWithShowedTrigger>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        meaningWithShowedTrigger = quizViewModel.getMeaningModel()

        return LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_meaning, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(meaningWithShowedTrigger) {
            wordValue.text = it.meaning.word.value
            wordTranslation.text = it.meaning.word.translation
        }
    }
}
package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentQuizMeaningBinding
import ru.egoncharovsky.words.ui.observe

class MeaningFragment : Fragment() {

    private val binding: FragmentQuizMeaningBinding by viewBinding()
    private val quizViewModel: QuizViewModel by viewModels(
        ownerProducer = { this.requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_meaning, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(quizViewModel.getMeaning()) {
            binding.wordValue.text = it.meaning.word.value
            binding.wordTranslation.text = it.meaning.word.translation
        }
    }
}
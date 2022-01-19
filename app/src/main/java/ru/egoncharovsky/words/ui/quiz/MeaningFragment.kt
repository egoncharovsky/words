package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.egoncharovsky.words.databinding.FragmentQuizMeaningBinding
import ru.egoncharovsky.words.ui.observe

class MeaningFragment : Fragment() {

    private lateinit var binding: FragmentQuizMeaningBinding
    private val quizViewModel: QuizViewModel by viewModels(
        ownerProducer = { this.requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizMeaningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(quizViewModel.getMeaning()) {
            binding.wordValue.text = it.meaning.word.value
            binding.wordTranslation.text = it.meaning.word.translation
        }
    }
}
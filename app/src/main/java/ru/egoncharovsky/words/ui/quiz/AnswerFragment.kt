package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentQuizAnswerBinding
import ru.egoncharovsky.words.ui.getColor
import ru.egoncharovsky.words.ui.observe

class AnswerFragment : Fragment() {

    private val binding: FragmentQuizAnswerBinding by viewBinding()
    private val quizViewModel: QuizViewModel by viewModels(
        ownerProducer = { this.requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_answer, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(quizViewModel.getAnswerWitchCallback()) { answerWithCallback ->
            binding.word.text = answerWithCallback.question.word.value
            binding.sendAnswer.isEnabled = true
            binding.sendAnswer.setOnClickListener {
                answerWithCallback.sendAnswer(binding.answerText.text.toString())
            }
        }
        observe(quizViewModel.getAnswerCorrectness()) {
            it?.let {
                if (it) {
                    binding.answerCorrect.text = getString(R.string.answer_correct)
                    binding.answerCorrect.setTextColor(getColor(R.color.correctLight))
                    binding.answerResult.text = getString(R.string.answer_result_good_job)
                    binding.answerResult.setTextColor(getColor(R.color.correct))
                } else {
                    binding.answerCorrect.text = getString(R.string.answer_incorrect)
                    binding.answerCorrect.setTextColor(getColor(R.color.incorrectLight))
                    binding.answerResult.text = getString(R.string.answer_result_lets_try_again)
                    binding.answerResult.setTextColor(getColor(R.color.incorrectLight))
                }
                binding.answerCorrect.visibility = View.VISIBLE
                binding.answerResult.visibility = View.VISIBLE
                binding.sendAnswer.isEnabled = false
            }
        }
    }
}
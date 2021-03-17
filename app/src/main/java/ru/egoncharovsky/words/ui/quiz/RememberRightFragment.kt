package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentQuizRememberRightBinding
import ru.egoncharovsky.words.domain.quiz.card.RememberRight
import ru.egoncharovsky.words.ui.observe

class RememberRightFragment : Fragment() {

    private val binding: FragmentQuizRememberRightBinding by viewBinding()
    private val quizViewModel: QuizViewModel by viewModels(
        ownerProducer = { this.requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_remember_right, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(quizViewModel.getRememberRightWithCallback()) { rememberRightWithCallback ->
            binding.wordValue.text = rememberRightWithCallback.question.word.value
            binding.wordTranslation.text = rememberRightWithCallback.question.word.translation

            binding.no.setOnClickListener {
                rememberRightWithCallback.sendAnswer(RememberRight.Option.NO)
                hideButtons()
            }
            binding.yes.setOnClickListener {
                rememberRightWithCallback.sendAnswer(RememberRight.Option.YES)
                hideButtons()
            }

            enableButtons()
        }
    }

    private fun hideButtons() = buttons().forEach { it.visibility = View.GONE }
    private fun enableButtons() = buttons().forEach { it.isEnabled = true }

    private fun buttons() = listOf(
        binding.no,
        binding.yes
    )
}
package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentQuizRememberBinding
import ru.egoncharovsky.words.domain.quiz.card.Remember
import ru.egoncharovsky.words.ui.observe

class RememberFragment : Fragment() {

    private val binding: FragmentQuizRememberBinding by viewBinding()
    private val quizViewModel: QuizViewModel by viewModels(
        ownerProducer = { this.requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_remember, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(quizViewModel.getRememberWithCallback()) { rememberWithCallback ->
            binding.wordValue.text = rememberWithCallback.question.word.value

            binding.no.setOnClickListener {
                rememberWithCallback.sendAnswer(Remember.Option.NO)
                hideButtons()
            }
            binding.maybe.setOnClickListener {
                rememberWithCallback.sendAnswer(Remember.Option.MAYBE)
                hideButtons()
            }
            binding.yes.setOnClickListener {
                rememberWithCallback.sendAnswer(Remember.Option.YES)
                hideButtons()
            }

            enableButtons()
        }
    }

    private fun hideButtons() = buttons().forEach { it.visibility = View.GONE }
    private fun enableButtons() = buttons().forEach { it.isEnabled = true }

    private fun buttons() = listOf(
        binding.no,
        binding.maybe,
        binding.yes
    )
}
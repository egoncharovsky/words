package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.egoncharovsky.words.databinding.FragmentQuizRememberBinding
import ru.egoncharovsky.words.domain.quiz.card.Remember
import ru.egoncharovsky.words.ui.observe

class RememberFragment : Fragment() {

    private lateinit var binding: FragmentQuizRememberBinding
    private val quizViewModel: QuizViewModel by viewModels(
        ownerProducer = { this.requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizRememberBinding.inflate(inflater, container, false)
        return binding.root
    }

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
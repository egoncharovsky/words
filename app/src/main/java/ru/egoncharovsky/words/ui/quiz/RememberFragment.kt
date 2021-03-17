package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_quiz_meaning.wordValue
import kotlinx.android.synthetic.main.fragment_quiz_remember.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.quiz.card.Remember
import ru.egoncharovsky.words.ui.observe

class RememberFragment : Fragment() {

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
            wordValue.text = rememberWithCallback.question.word.value

            no.setOnClickListener {
                rememberWithCallback.sendAnswer(Remember.Option.NO)
                buttons().forEach { it.visibility = View.GONE }
            }
            maybe.setOnClickListener {
                rememberWithCallback.sendAnswer(Remember.Option.MAYBE)
                buttons().forEach { it.visibility = View.GONE }
            }
            yes.setOnClickListener {
                rememberWithCallback.sendAnswer(Remember.Option.YES)
                buttons().forEach { it.visibility = View.GONE }
            }

            buttons().forEach { it.isEnabled = true }
        }
    }

    private fun buttons() = listOf(no, maybe, yes)
}
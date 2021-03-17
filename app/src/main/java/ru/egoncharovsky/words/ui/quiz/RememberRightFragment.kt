package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_quiz_meaning.*
import kotlinx.android.synthetic.main.fragment_quiz_meaning.wordValue
import kotlinx.android.synthetic.main.fragment_quiz_remember.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.quiz.card.RememberRight
import ru.egoncharovsky.words.ui.observe

class RememberRightFragment : Fragment() {

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
            wordValue.text = rememberRightWithCallback.question.word.value
            wordTranslation.text = rememberRightWithCallback.question.word.translation

            no.setOnClickListener {
                rememberRightWithCallback.sendAnswer(RememberRight.Option.NO)
                buttons().forEach { it.visibility = View.GONE }
            }
            yes.setOnClickListener {
                rememberRightWithCallback.sendAnswer(RememberRight.Option.YES)
                buttons().forEach { it.visibility = View.GONE }
            }

            buttons().forEach { it.isEnabled = true }
        }
    }

    private fun buttons() = listOf(no, yes)
}
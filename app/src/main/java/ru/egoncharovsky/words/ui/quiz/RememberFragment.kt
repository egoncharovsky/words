package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.fragment_quiz_meaning.wordValue
import kotlinx.android.synthetic.main.fragment_quiz_remember.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.quiz.card.Remember
import ru.egoncharovsky.words.ui.observe

class RememberFragment : Fragment() {

    private val quizViewModel: QuizViewModel by activityViewModels()

    private lateinit var rememberWithCallback: LiveData<QuizViewModel.QuestionWithCallback<Remember, Remember.Option>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rememberWithCallback = quizViewModel.getRememberModel()

        return LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_remember, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(rememberWithCallback) { model ->
            wordValue.text = model.question.word.value
            buttons().forEach { it.isEnabled = true }
        }
        no.setOnClickListener {
            rememberWithCallback.value?.sendAnswer(Remember.Option.NO)
            buttons().forEach { it.visibility = View.GONE }
        }
        maybe.setOnClickListener {
            rememberWithCallback.value?.sendAnswer(Remember.Option.MAYBE)
            buttons().forEach { it.visibility = View.GONE }
        }
        yes.setOnClickListener {
            rememberWithCallback.value?.sendAnswer(Remember.Option.YES)
            buttons().forEach { it.visibility = View.GONE }
        }
    }

    private fun buttons() = listOf(no, maybe, yes)
}
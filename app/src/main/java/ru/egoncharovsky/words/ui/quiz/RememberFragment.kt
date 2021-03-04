package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.fragment_quiz_meaning.wordValue
import kotlinx.android.synthetic.main.fragment_quiz_remember.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.quiz.card.Remember

class RememberFragment(
    private val rememberWithCallback: LiveData<QuizViewModel.QuestionWithCallback<Remember, Remember.Option>>
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_remember, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rememberWithCallback.observe(viewLifecycleOwner) {
            wordValue.text = it.question.word.value
        }
        no.setOnClickListener {
            rememberWithCallback.value?.sendAnswer(Remember.Option.NO)
        }
        maybe.setOnClickListener {
            rememberWithCallback.value?.sendAnswer(Remember.Option.MAYBE)
        }
        yes.setOnClickListener {
            rememberWithCallback.value?.sendAnswer(Remember.Option.YES)
        }

    }
}
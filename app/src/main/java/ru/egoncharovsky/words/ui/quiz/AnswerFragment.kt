package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.fragment_quiz_answer.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.quiz.card.Answer

class AnswerFragment(
    private val answerWithCallback: LiveData<QuizViewModel.QuestionWithCallback<Answer, String>>
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_answer, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        answerWithCallback.observe(viewLifecycleOwner) {
            word.text = it.question.word.value
        }
        sendAnswer.setOnClickListener {
            answerWithCallback.value?.sendAnswer(answerText.text.toString())
        }
    }
}
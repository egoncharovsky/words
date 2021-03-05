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
import ru.egoncharovsky.words.ui.getColor
import ru.egoncharovsky.words.ui.observe

class AnswerFragment(
    private val answerWithCallback: LiveData<QuizViewModel.QuestionWithCallback<Answer, String>>,
    private val answerCorrectness: LiveData<Boolean?>
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_answer, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(answerWithCallback) {
            word.text = it.question.word.value
            sendAnswer.isEnabled = true
        }
        observe(answerCorrectness) {
            it?.let {
                if (it) {
                    answerCorrect.text = getString(R.string.answer_correct)
                    answerCorrect.setTextColor(getColor(R.color.colorCorrectLight))
                    answerResult.text = getString(R.string.answer_result_good_job)
                    answerResult.setTextColor(getColor(R.color.colorCorrect))
                } else {
                    answerCorrect.text = getString(R.string.answer_incorrect)
                    answerCorrect.setTextColor(getColor(R.color.colorIncorrectLight))
                    answerResult.text = getString(R.string.answer_result_lets_try_again)
                    answerResult.setTextColor(getColor(R.color.colorIncorrectLight))
                }
                answerCorrect.visibility = View.VISIBLE
                answerResult.visibility = View.VISIBLE
                sendAnswer.isEnabled = false
            }
        }
        sendAnswer.setOnClickListener {
            answerWithCallback.value?.sendAnswer(answerText.text.toString())
        }
    }
}
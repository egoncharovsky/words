package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.fragment_quiz_answer.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.quiz.card.Answer
import ru.egoncharovsky.words.ui.getColor
import ru.egoncharovsky.words.ui.observe

class AnswerFragment : Fragment() {

    private val quizViewModel: QuizViewModel by activityViewModels()

    private lateinit var answerWithCallback: LiveData<QuizViewModel.QuestionWithCallback<Answer, String>>
    private lateinit var answerCorrectness: LiveData<Boolean?>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        answerWithCallback = quizViewModel.getAnswerModel()
        answerCorrectness = quizViewModel.getAnswerCorrectness()

        return LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_answer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(answerWithCallback) {
            word.text = it.question.word.value
            sendAnswer.isEnabled = true
        }
        observe(answerCorrectness) {
            it?.let {
                if (it) {
                    answerCorrect.text = getString(R.string.answer_correct)
                    answerCorrect.setTextColor(getColor(R.color.correctLight))
                    answerResult.text = getString(R.string.answer_result_good_job)
                    answerResult.setTextColor(getColor(R.color.correct))
                } else {
                    answerCorrect.text = getString(R.string.answer_incorrect)
                    answerCorrect.setTextColor(getColor(R.color.incorrectLight))
                    answerResult.text = getString(R.string.answer_result_lets_try_again)
                    answerResult.setTextColor(getColor(R.color.incorrectLight))
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
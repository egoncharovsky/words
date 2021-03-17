package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_quiz_answer.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.ui.getColor
import ru.egoncharovsky.words.ui.observe

class AnswerFragment : Fragment() {

    private val quizViewModel: QuizViewModel by viewModels(
        ownerProducer = { this.requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_answer, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(quizViewModel.getAnswerWitchCallback()) { answerWithCallback ->
            word.text = answerWithCallback.question.word.value
            sendAnswer.isEnabled = true
            sendAnswer.setOnClickListener {
                answerWithCallback.sendAnswer(answerText.text.toString())
            }
        }
        observe(quizViewModel.getAnswerCorrectness()) {
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
    }
}
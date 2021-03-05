package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.fragment_quiz_answer.*
import kotlinx.android.synthetic.main.fragment_quiz_meaning.wordValue
import kotlinx.android.synthetic.main.fragment_quiz_multiple_choice.*
import kotlinx.android.synthetic.main.fragment_quiz_multiple_choice.answerResult
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.quiz.card.MultiChoice
import ru.egoncharovsky.words.domain.quiz.card.Question
import ru.egoncharovsky.words.ui.observe

class MultiChoiceFragment(
    private val multiChoiceWithCallback: LiveData<QuizViewModel.QuestionWithCallback<MultiChoice, String>>,
    private val answerCorrectness: LiveData<Boolean?>
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_multiple_choice, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(multiChoiceWithCallback) { model ->
            wordValue.text = model.question.word.value

            listOf(option1, option2, option3, option4, option5).forEachIndexed { index, button ->
                model.question.options.elementAtOrNull(index)?.let { value ->
                    button.text = value
                    button.setOnClickListener {
                        model.sendAnswer(value)

                        highlightAnswer(model.question, value, button)
                    }
                    button.isEnabled = true
                } ?: run {
                    button.visibility = View.INVISIBLE
                }
            }
        }
        observe(answerCorrectness) {
            it?.let {
                if (it) {
                    answerResult.text = getString(R.string.answer_result_good_job)
                    answerResult.setTextColor(R.color.colorCorrect)
                } else {
                    answerResult.text = getString(R.string.answer_result_lets_try_again)
                    answerResult.setTextColor(R.color.colorIncorrectLight)
                }
                answerResult.visibility = View.VISIBLE
                listOf(option1, option2, option3, option4, option5).forEach { button ->  button.isEnabled = false }
            }
        }
    }

    private fun highlightAnswer(question: MultiChoice, value: String, clicked: Button) {
        if (!question.checkAnswer(value)) {
            clicked.setBackgroundColor(R.color.colorIncorrectLight)
        }

        listOf(option1, option2, option3, option4, option5)
            .find { it.text == question.correct }?.setBackgroundColor(R.color.colorCorrectLight)
    }
}
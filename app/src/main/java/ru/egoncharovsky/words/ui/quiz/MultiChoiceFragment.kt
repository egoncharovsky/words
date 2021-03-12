package ru.egoncharovsky.words.ui.quiz

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.fragment_quiz_meaning.wordValue
import kotlinx.android.synthetic.main.fragment_quiz_multiple_choice.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.quiz.card.MultiChoice
import ru.egoncharovsky.words.ui.getColor
import ru.egoncharovsky.words.ui.observe

class MultiChoiceFragment : Fragment() {

    private val quizViewModel: QuizViewModel by activityViewModels()

    private lateinit var multiChoiceWithCallback: LiveData<QuizViewModel.QuestionWithCallback<MultiChoice, String>>
    private lateinit var answerCorrectness: LiveData<Boolean?>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        multiChoiceWithCallback = quizViewModel.getMultiChoiceModel()
        answerCorrectness = quizViewModel.getAnswerCorrectness()

        return LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_multiple_choice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(multiChoiceWithCallback) { model ->
            wordValue.text = model.question.word.value

            buttons().forEachIndexed { index, button ->
                model.question.options.elementAtOrNull(index)?.let { value ->
                    button.text = value
                    button.setOnClickListener {
                        model.sendAnswer(value)

                        if (!model.question.checkAnswer(value)) {
                            highlightIncorrectAnswer(button)
                        }
                    }
                    button.setTag(R.id.button_right_option_tag, model.question.correct == value)

                    button.visibility = View.VISIBLE
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
                    answerResult.setTextColor(getColor(R.color.correct))
                } else {
                    answerResult.text = getString(R.string.answer_result_lets_try_again)
                    answerResult.setTextColor(getColor(R.color.incorrectLight))
                }
                answerResult.visibility = View.VISIBLE

                highlightButtons()
            }
        }
    }

    private fun buttons() = listOf(option1, option2, option3, option4, option5)

    private fun highlightButtons() {
        buttons().forEach { button ->
            val rightOption = button.getTag(R.id.button_right_option_tag)?.let { it as Boolean } ?: false

            if (rightOption) {
                highlightCorrectAnswer(button)
            }
            button.isEnabled = false
        }
    }

    fun highlightCorrectAnswer(button: Button) {
        button.setBackgroundColor(getColor(R.color.correctLight))
        button.setTextColor(getColor(R.color.blackText))
        boldText(button)
    }

    fun highlightIncorrectAnswer(button: Button) {
        button.setBackgroundColor(getColor(R.color.incorrectLight))
        button.setTextColor(getColor(R.color.blackText))
        boldText(button)
    }

    private fun boldText(button: Button) {
        button.text =
            SpannableString(button.text).apply { setSpan(StyleSpan(Typeface.BOLD), 0, button.text.length, 0) }
    }
}
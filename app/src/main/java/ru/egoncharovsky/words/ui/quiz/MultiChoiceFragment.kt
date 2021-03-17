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
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentQuizMultipleChoiceBinding
import ru.egoncharovsky.words.ui.getColor
import ru.egoncharovsky.words.ui.observe

class MultiChoiceFragment : Fragment() {

    private val binding: FragmentQuizMultipleChoiceBinding by viewBinding()
    private val quizViewModel: QuizViewModel by viewModels(
        ownerProducer = { this.requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_multiple_choice, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(quizViewModel.getMultiChoiceWithCallback()) { model ->
            binding.wordValue.text = model.question.word.value

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
        observe(quizViewModel.getAnswerCorrectness()) {
            it?.let {
                if (it) {
                    binding.answerResult.text = getString(R.string.answer_result_good_job)
                    binding.answerResult.setTextColor(getColor(R.color.correct))
                } else {
                    binding.answerResult.text = getString(R.string.answer_result_lets_try_again)
                    binding.answerResult.setTextColor(getColor(R.color.incorrectLight))
                }
                binding.answerResult.visibility = View.VISIBLE

                highlightButtons()
            }
        }
    }

    private fun buttons() = listOf(
        binding.option1,
        binding.option2,
        binding.option3,
        binding.option4,
        binding.option5
    )

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
package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.fragment_quiz_meaning.wordValue
import kotlinx.android.synthetic.main.fragment_quiz_multiple_choice.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.quiz.card.MultiChoice

class MultiChoiceFragment(
    private val multiChoiceWithCallback: LiveData<QuizViewModel.QuestionWithCallback<MultiChoice, String>>
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_multiple_choice, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        multiChoiceWithCallback.observe(viewLifecycleOwner) { model ->
            wordValue.text = model.question.word.value

            listOf(option1, option2, option3, option4, option5).withIndex().forEach { button ->
                model.question.options.elementAtOrNull(button.index)?.let { value ->
                    button.value.text = value
                    button.value.setOnClickListener {
                        model.sendAnswer(value)
                    }
                } ?: run {
                    button.value.visibility = View.INVISIBLE
                }
            }
        }
    }
}
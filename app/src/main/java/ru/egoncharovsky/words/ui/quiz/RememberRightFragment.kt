package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.fragment_quiz_answer.*
import kotlinx.android.synthetic.main.fragment_quiz_meaning.*
import kotlinx.android.synthetic.main.fragment_quiz_meaning.wordValue
import kotlinx.android.synthetic.main.fragment_quiz_remember.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.quiz.card.Remember
import ru.egoncharovsky.words.domain.quiz.card.RememberRight

class RememberRightFragment(
    private val rememberRightWithCallback: LiveData<QuizViewModel.QuestionWithCallback<RememberRight, RememberRight.Option>>
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_remember_right, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rememberRightWithCallback.observe(viewLifecycleOwner) {
            wordValue.text = it.question.word.value
            wordTranslation.text = it.question.word.translation
        }
        no.setOnClickListener {
            rememberRightWithCallback.value?.sendAnswer(RememberRight.Option.NO)
        }
        yes.setOnClickListener {
            rememberRightWithCallback.value?.sendAnswer(RememberRight.Option.YES)
        }

    }
}
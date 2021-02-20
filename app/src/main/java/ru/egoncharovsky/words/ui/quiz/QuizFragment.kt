package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.egoncharovsky.words.MainActivity
import ru.egoncharovsky.words.R

class QuizFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        val root = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz, container, false)

        if (MainActivity.lastQuiz is AnswerFragment) {
            MainActivity.lastQuiz = MultiChoiceFragment()
        } else if (MainActivity.lastQuiz is MultiChoiceFragment) {
            MainActivity.lastQuiz = MeaningFragment()
        } else if (MainActivity.lastQuiz is MeaningFragment) {
            MainActivity.lastQuiz = AnswerFragment()
        }
        childFragmentManager.beginTransaction().add(R.id.quiz_replacement, MainActivity.lastQuiz).commit()

        return root
    }


}
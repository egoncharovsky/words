package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        childFragmentManager.beginTransaction().add(R.id.quiz_replacement, MainActivity.lastQuiz).commit()

        root.findViewById<Button>(R.id.quiz_next).setOnClickListener { nextCard() }

        return root
    }

    fun nextCard() {
        val newQuiz = when (MainActivity.lastQuiz) {
            is AnswerFragment -> {
                MultiChoiceFragment()
            }
            is MultiChoiceFragment -> {
                MeaningFragment()
            }
            is MeaningFragment -> {
                RememberFragment()
            }
            is RememberFragment -> {
                AnswerFragment()
            }
            else -> throw Exception()
        }

        childFragmentManager.beginTransaction().remove(MainActivity.lastQuiz).add(R.id.quiz_replacement, newQuiz).commit()
        MainActivity.lastQuiz = newQuiz
    }

}
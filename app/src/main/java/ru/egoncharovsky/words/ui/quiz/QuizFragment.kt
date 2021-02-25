package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.egoncharovsky.words.R

class QuizFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel

    private lateinit var nextButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        val root = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz, container, false)
        nextButton = root.findViewById(R.id.quiz_next)

        nextButton.setOnClickListener { nextCard() }

        lastQuiz = AnswerFragment(nextButton)
        childFragmentManager.beginTransaction().add(R.id.quiz_replacement, lastQuiz).commit()

        return root
    }

    companion object {
        lateinit var lastQuiz: Fragment
    }

    fun nextCard() {
        nextButton.visibility = View.INVISIBLE
        val newQuiz = when (lastQuiz) {
            is AnswerFragment -> {
                MultiChoiceFragment(nextButton)
            }
            is MultiChoiceFragment -> {
                MeaningFragment(nextButton)
            }
            is MeaningFragment -> {
                RememberFragment(nextButton)
            }
            is RememberFragment -> {
                AnswerFragment(nextButton)
            }
            else -> throw Exception()
        }

        childFragmentManager.beginTransaction().remove(lastQuiz).add(R.id.quiz_replacement, newQuiz).commit()
        lastQuiz = newQuiz
    }

}
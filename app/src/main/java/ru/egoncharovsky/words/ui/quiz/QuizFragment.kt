package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.Word
import ru.egoncharovsky.words.domain.quiz.QuizManager

class QuizFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel

    private lateinit var nextButton: Button

    private val dictionary = listOf(
        Word("apple", "яблоко"),
        Word("any", "любой"),
        Word("you", "ты"),
        Word("I", "я"),
        Word("many", "много"),
        Word("love", "любить"),
        Word("TV", "телевизор"),
        Word("shift", "сдвиг"),
        Word("weather", "погода"),
        Word("translation", "перевод"),
        Word("nice", "приятный"),
        Word("very", "очень"),
        Word("Greece", "Греция"),
        Word("cool", "круто"),
        Word("cold", "холодно"),
        Word("hot", "горячо"),
        Word("hungry", "голодный"),
        Word("breakfast", "завтра"),
    )

    private val quizManager = QuizManager(dictionary.toSet())

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
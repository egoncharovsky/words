package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_quiz.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.quiz.card.Card

class QuizFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        return LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        quizViewModel.getCard().observe(viewLifecycleOwner, { card ->
            childFragmentManager.beginTransaction().add(
                R.id.quiz_replacement, when (card.type()) {
                    Card.Type.ANSWER -> AnswerFragment(quizViewModel.getAnswerModel())
                    Card.Type.MEANING -> MeaningFragment(quizViewModel.getMeaningModel())
                    Card.Type.MULTI_CHOICE -> MultiChoiceFragment(quizViewModel.getMultiChoiceModel())
                    Card.Type.REMEMBER -> RememberFragment(quizViewModel.getRememberModel())
                    Card.Type.REMEMBER_RIGHT -> RememberRightFragment(quizViewModel.getRememberRightModel())
                }
            ).commit()
        })
        quizViewModel.getNextVisibility().observe(viewLifecycleOwner) { visible ->
            if (visible) {
                nextButton.visibility = View.VISIBLE
            } else {
                nextButton.visibility = View.INVISIBLE
            }
        }
        nextButton.setOnClickListener {
            if (childFragmentManager.fragments.isNotEmpty()) {
                val transaction = childFragmentManager.beginTransaction()
                childFragmentManager.fragments.forEach { transaction.remove(it) }
                transaction.commitNow()
            }
            quizViewModel.clickNext()
        }
        quizViewModel.getProgress().observe(viewLifecycleOwner) {
            progressBar.progress = it
        }
        quizViewModel.getFinished().observe(viewLifecycleOwner) {
            Snackbar.make(view, "Test finished!", Snackbar.LENGTH_LONG).show()
        }
        timeIndicator.visibility = View.INVISIBLE
    }

}
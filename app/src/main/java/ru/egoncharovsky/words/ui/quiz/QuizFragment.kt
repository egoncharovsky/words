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
import ru.egoncharovsky.words.ui.observe

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
        observe(quizViewModel.getCard()) { card ->
            childFragmentManager.beginTransaction().add(
                R.id.quiz_replacement, when (card.type()) {
                    Card.Type.ANSWER ->
                        AnswerFragment(quizViewModel.getAnswerModel(), quizViewModel.getAnswerCorrectness())
                    Card.Type.MEANING ->
                        MeaningFragment(quizViewModel.getMeaningModel())
                    Card.Type.MULTI_CHOICE ->
                        MultiChoiceFragment(quizViewModel.getMultiChoiceModel(), quizViewModel.getAnswerCorrectness())
                    Card.Type.REMEMBER ->
                        RememberFragment(quizViewModel.getRememberModel())
                    Card.Type.REMEMBER_RIGHT ->
                        RememberRightFragment(quizViewModel.getRememberRightModel())
                }
            ).commit()
        }
        observe(quizViewModel.getNextVisibility()) { visible ->
            if (visible) {
                nextButton.visibility = View.VISIBLE
            } else {
                nextButton.visibility = View.GONE
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
        observe(quizViewModel.getProgress()) {
            progressBar.progress = it
        }
        observe(quizViewModel.getFinished()) {
            if (it) {
                Snackbar.make(view, "Test finished!", Snackbar.LENGTH_LONG).show()
                nextButton.visibility = View.INVISIBLE
            }
        }
        timeIndicator.visibility = View.INVISIBLE
    }

}
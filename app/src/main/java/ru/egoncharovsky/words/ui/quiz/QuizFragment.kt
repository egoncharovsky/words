package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_quiz.*
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.domain.quiz.card.Card
import ru.egoncharovsky.words.ui.observe

class QuizFragment : Fragment() {

    private val quizViewModel: QuizViewModel by activityViewModels()
    private val args: QuizFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        quizViewModel.startQuiz(args.studyListId)

        observe(quizViewModel.getCard()) { card ->
            childFragmentManager.beginTransaction().add(
                R.id.quiz_replacement, when (card.type()) {
                    Card.Type.ANSWER -> AnswerFragment()
                    Card.Type.MEANING -> MeaningFragment()
                    Card.Type.MULTI_CHOICE -> MultiChoiceFragment()
                    Card.Type.REMEMBER -> RememberFragment()
                    Card.Type.REMEMBER_RIGHT -> RememberRightFragment()
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
            clearChildFragments()
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

    private fun clearChildFragments() {
        if (childFragmentManager.fragments.isNotEmpty()) {
            val transaction = childFragmentManager.beginTransaction()
            childFragmentManager.fragments.forEach { transaction.remove(it) }
            transaction.commitNow()
        }
    }
}
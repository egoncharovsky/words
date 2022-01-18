package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentQuizBinding
import ru.egoncharovsky.words.domain.quiz.card.Card
import ru.egoncharovsky.words.ui.observe

@AndroidEntryPoint
class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private val args: QuizFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

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
                binding.nextButton.visibility = View.VISIBLE
            } else {
                binding.nextButton.visibility = View.GONE
            }
        }
        binding.nextButton.setOnClickListener {
            clearChildFragments()
            quizViewModel.clickNext()
        }
        observe(quizViewModel.getProgress()) {
            binding.progressBar.progress = it
        }
        observe(quizViewModel.getFinished()) {
            if (it) {
                Snackbar.make(view, "Test finished!", Snackbar.LENGTH_LONG).show()
                binding.nextButton.visibility = View.INVISIBLE
            }
        }
        binding.timeIndicator.visibility = View.INVISIBLE
    }

    private fun clearChildFragments() {
        if (childFragmentManager.fragments.isNotEmpty()) {
            val transaction = childFragmentManager.beginTransaction()
            childFragmentManager.fragments.forEach { transaction.remove(it) }
            transaction.commitNow()
        }
    }
}
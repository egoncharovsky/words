package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.egoncharovsky.words.R

class AnswerFragment(
    private val nextButton: Button
) : Fragment() {

    private lateinit var answerViewModel: AnswerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        answerViewModel = ViewModelProvider(this).get(AnswerViewModel::class.java)
        val root = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_answer, container, false)

        root.findViewById<Button>(R.id.quiz_answer_send).setOnClickListener { sendAnswer() }

        return root
    }

    private fun sendAnswer() {
        nextButton.visibility = View.VISIBLE
    }
}
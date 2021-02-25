package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.egoncharovsky.words.R

class RememberFragment(
    private val nextButton: Button
) : Fragment() {

    private lateinit var rememberViewModel: RememberViewModel

    private lateinit var yesButton: Button
    private lateinit var maybeButton: Button
    private lateinit var noButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rememberViewModel = ViewModelProvider(this).get(RememberViewModel::class.java)
        val root = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_remember, container, false)

        yesButton = root.findViewById(R.id.quiz_remember_yes)
        maybeButton = root.findViewById(R.id.quiz_remember_maybe)
        noButton = root.findViewById(R.id.quiz_remember_no)

        yesButton.setOnClickListener { sendAnswer() }
        maybeButton.setOnClickListener { sendAnswer() }
        noButton.setOnClickListener { sendAnswer() }

        return root
    }

    private fun sendAnswer() {
        nextButton.visibility = View.VISIBLE
    }
}
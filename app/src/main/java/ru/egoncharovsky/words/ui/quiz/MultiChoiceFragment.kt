package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.egoncharovsky.words.R

class MultiChoiceFragment(
    private val nextButton: Button
) : Fragment() {

    private lateinit var multiChoiceViewModel: MultiChoiceViewModel

    private lateinit var multiChoiceButton1: Button
    private lateinit var multiChoiceButton2: Button
    private lateinit var multiChoiceButton3: Button
    private lateinit var multiChoiceButton4: Button
    private lateinit var multiChoiceButton5: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        multiChoiceViewModel = ViewModelProvider(this).get(MultiChoiceViewModel::class.java)
        val root = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_multiple_choice, container, false)

        multiChoiceButton1 = root.findViewById(R.id.quiz_multiChoiceButton1)
        multiChoiceButton2 = root.findViewById(R.id.quiz_multiChoiceButton2)
        multiChoiceButton3 = root.findViewById(R.id.quiz_multiChoiceButton3)
        multiChoiceButton4 = root.findViewById(R.id.quiz_multiChoiceButton4)
        multiChoiceButton5 = root.findViewById(R.id.quiz_multiChoiceButton5)

        multiChoiceButton1.setOnClickListener { sendAnswer() }
        multiChoiceButton2.setOnClickListener { sendAnswer() }
        multiChoiceButton3.setOnClickListener { sendAnswer() }
        multiChoiceButton4.setOnClickListener { sendAnswer() }
        multiChoiceButton5.setOnClickListener { sendAnswer() }

        return root
    }

    private fun sendAnswer() {
        nextButton.visibility = View.VISIBLE
    }
}
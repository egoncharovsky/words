package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.egoncharovsky.words.R

class MeaningFragment(
    private val nextButton: Button
) : Fragment() {

    private lateinit var meaningViewModel: MeaningViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        meaningViewModel = ViewModelProvider(this).get(MeaningViewModel::class.java)
        val root = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_meaning, container, false)

        nextButton.visibility = View.VISIBLE

        return root
    }
}
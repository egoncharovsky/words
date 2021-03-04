package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.fragment_quiz_meaning.*
import ru.egoncharovsky.words.R

class MeaningFragment(
    private val meaningWithShowedTrigger: LiveData<QuizViewModel.MeaningWithShowedTrigger>
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_meaning, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        meaningWithShowedTrigger.observe(viewLifecycleOwner) {
            wordValue.text = it.meaning.word.value
        }
    }
}
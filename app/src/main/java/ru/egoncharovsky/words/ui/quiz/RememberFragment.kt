package ru.egoncharovsky.words.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.egoncharovsky.words.R

class RememberFragment : Fragment() {

    private lateinit var rememberViewModel: RememberViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rememberViewModel = ViewModelProvider(this).get(RememberViewModel::class.java)
        val root = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_quiz_remember, container, false)

        return root
    }


}
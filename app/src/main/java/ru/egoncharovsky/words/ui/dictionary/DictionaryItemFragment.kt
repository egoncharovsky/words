package ru.egoncharovsky.words.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.egoncharovsky.words.R

class DictionaryItemFragment : Fragment() {

    private lateinit var dictionaryItemViewModel: DictionaryItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dictionaryItemViewModel = ViewModelProvider(this).get(DictionaryItemViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dictionary_item, container, false)

        return root
    }
}
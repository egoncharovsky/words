package ru.egoncharovsky.words.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.egoncharovsky.words.R

class DictionaryFragment : Fragment() {

    private lateinit var dictionaryViewModel: DictionaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dictionaryViewModel = ViewModelProvider(this).get(DictionaryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dictionary, container, false)

        childFragmentManager.beginTransaction()
            .add(R.id.dictionary_list, DictionaryItem(), "1")
            .add(R.id.dictionary_list, DictionaryItem(), "2")
            .add(R.id.dictionary_list, DictionaryItem(), "3")
            .add(R.id.dictionary_list, DictionaryItem(), "4")
            .add(R.id.dictionary_list, DictionaryItem(), "5")
            .add(R.id.dictionary_list, DictionaryItem(), "6")
            .add(R.id.dictionary_list, DictionaryItem(), "7")
            .add(R.id.dictionary_list, DictionaryItem(), "8")
            .add(R.id.dictionary_list, DictionaryItem(), "9")
            .add(R.id.dictionary_list, DictionaryItem(), "10")
            .add(R.id.dictionary_list, DictionaryItem(), "11")
            .add(R.id.dictionary_list, DictionaryItem(), "12")
            .add(R.id.dictionary_list, DictionaryItem(), "13")
            .add(R.id.dictionary_list, DictionaryItem(), "14")
            .add(R.id.dictionary_list, DictionaryItem(), "15")
            .add(R.id.dictionary_list, DictionaryItem(), "16")
            .add(R.id.dictionary_list, DictionaryItem(), "17")
            .add(R.id.dictionary_list, DictionaryItem(), "18")
            .add(R.id.dictionary_list, DictionaryItem(), "19")
            .add(R.id.dictionary_list, DictionaryItem(), "20")
            .add(R.id.dictionary_list, DictionaryItem(), "21")
            .add(R.id.dictionary_list, DictionaryItem(), "22")
            .add(R.id.dictionary_list, DictionaryItem(), "23")
            .add(R.id.dictionary_list, DictionaryItem(), "24")
            .add(R.id.dictionary_list, DictionaryItem(), "25")
            .commit()


        return root
    }
}
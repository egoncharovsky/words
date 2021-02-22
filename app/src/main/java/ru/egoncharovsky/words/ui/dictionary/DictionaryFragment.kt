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
            .add(R.id.dictionary_list, DictionaryItemFragment(), "1")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "2")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "3")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "4")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "5")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "6")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "7")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "8")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "9")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "10")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "11")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "12")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "13")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "14")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "15")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "16")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "17")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "18")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "19")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "20")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "21")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "22")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "23")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "24")
            .add(R.id.dictionary_list, DictionaryItemFragment(), "25")
            .commit()


        return root
    }
}
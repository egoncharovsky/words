package ru.egoncharovsky.words.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentDictionaryBinding
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.ui.RecyclerViewAdapter
import ru.egoncharovsky.words.ui.dictionary.search.WordSearchWidget

@AndroidEntryPoint
open class DictionaryFragment : Fragment() {

    protected val binding: FragmentDictionaryBinding by viewBinding()
    protected lateinit var dictionaryViewModel: DictionaryViewModel
    protected lateinit var dictionaryAdapter: RecyclerViewAdapter<Word, *>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dictionaryViewModel = ViewModelProvider(this).get(DictionaryViewModel::class.java)
        dictionaryAdapter = WordAdapter()

        return inflater.inflate(R.layout.fragment_dictionary, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.dictionaryList.layoutManager = LinearLayoutManager(view.context)
        binding.dictionaryList.adapter = dictionaryAdapter

        WordSearchWidget(
            binding.sortButton,
            binding.search,
            dictionaryViewModel,
            this
        ).onViewCreated(view) {
            dictionaryAdapter.update(it)
        }
    }
}
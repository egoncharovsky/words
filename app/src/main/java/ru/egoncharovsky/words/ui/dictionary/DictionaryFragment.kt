package ru.egoncharovsky.words.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.egoncharovsky.words.databinding.FragmentDictionaryBinding
import ru.egoncharovsky.words.databinding.FragmentDictionaryItemBinding
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.ui.RecyclerViewAdapter
import ru.egoncharovsky.words.ui.dictionary.search.WordSearchWidget

@AndroidEntryPoint
open class DictionaryFragment : Fragment() {

    private lateinit var binding: FragmentDictionaryBinding

    private val viewModel: DictionaryViewModel by viewModels()
    private lateinit var adapter: RecyclerViewAdapter<Word, FragmentDictionaryItemBinding>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = WordAdapter()

        binding = FragmentDictionaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.dictionaryList.layoutManager = LinearLayoutManager(view.context)
        binding.dictionaryList.adapter = adapter

        WordSearchWidget(binding.sortButton, binding.search, viewModel, this)
            .onViewCreated(view) {
                adapter.update(it)
            }
    }
}
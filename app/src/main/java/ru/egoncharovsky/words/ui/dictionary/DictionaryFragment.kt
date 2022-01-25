package ru.egoncharovsky.words.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.egoncharovsky.words.R
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
        adapter = WordAdapter(this::getPopularity)

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

    private fun getPopularity(id: Long): String? {
        return viewModel.getPopularityOf(id)?.let {
            String.format(getString(R.string.popularity), it)
        }
    }

    class WordAdapter(
        val getPopularity: (Long) -> String?
    ) : RecyclerViewAdapter<Word, FragmentDictionaryItemBinding>() {
        override val bindingInflate: (inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> FragmentDictionaryItemBinding =
            FragmentDictionaryItemBinding::inflate

        override fun bind(binding: FragmentDictionaryItemBinding, item: Word) {
            binding.wordValue.text = item.value
            binding.wordTranslation.text = item.translation
            getPopularity(item.id!!)?.let {
                binding.wordPopularity.text = it
                binding.wordPopularity.visibility = VISIBLE
            } ?: run {
                binding.wordPopularity.visibility = GONE
            }
        }
    }
}
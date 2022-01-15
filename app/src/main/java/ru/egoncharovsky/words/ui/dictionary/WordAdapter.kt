package ru.egoncharovsky.words.ui.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.egoncharovsky.words.databinding.FragmentDictionaryItemBinding
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.ui.RecyclerViewAdapter

class WordAdapter : RecyclerViewAdapter<Word, FragmentDictionaryItemBinding>() {
    override val bindingInflate: (inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> FragmentDictionaryItemBinding =
        FragmentDictionaryItemBinding::inflate

    override fun bind(binding: FragmentDictionaryItemBinding, item: Word) {
        binding.wordValue.text = item.value
        binding.wordTranslation.text = item.translation
    }
}
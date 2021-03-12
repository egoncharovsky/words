package ru.egoncharovsky.words.repository

import mu.KotlinLogging
import ru.egoncharovsky.words.domain.DictionaryEntry
import ru.egoncharovsky.words.domain.Language
import ru.egoncharovsky.words.domain.Word

object DictionaryEntryRepository : InMemoryRepository<Long, DictionaryEntry>(LongIdGenerator()) {

    private val logger = KotlinLogging.logger {}

    fun searchWord(value: String) = entities.values.filter {
        logger.debug("Search $value")
        it.word.value.contains(value, ignoreCase = true) || it.word.translation.contains(value, ignoreCase = true)
    }

    init {
        listOf(
            wordEnRu("apple", "яблоко"),
            wordEnRu("any", "любой"),
            wordEnRu("you", "ты"),
            wordEnRu("I", "я"),
            wordEnRu("many", "много"),
            wordEnRu("love", "любить"),
            wordEnRu("TV", "телевизор"),
            wordEnRu("shift", "сдвиг"),
            wordEnRu("weather", "погода"),
            wordEnRu("translation", "перевод"),
            wordEnRu("nice", "приятный"),
            wordEnRu("very", "очень"),
            wordEnRu("Greece", "Греция"),
            wordEnRu("cool", "круто"),
            wordEnRu("cold", "холодно"),
            wordEnRu("hot", "горячо"),
            wordEnRu("hungry", "голодный"),
            wordEnRu("breakfast", "завтрак"),
            wordEnRu("flower", "цветок"),
            wordEnRu("cub", "детеныш"),
            wordEnRu("pagan", "язычный"),
            wordEnRu("smart", "умный"),
            wordEnRu("prudent", "благоразумный"),
            wordEnRu("story", "история"),
            wordEnRu("mouse", "мышь"),
            wordEnRu("cup", "чашка"),
            wordEnRu("mag", "кружка"),
            wordEnRu("tea", "чай"),
            wordEnRu("box", "коробка"),
            wordEnRu("list", "список"),
            wordEnRu("blanket", "одеяло"),
            wordEnRu("eye", "глаз"),
            wordEnRu("ear", "ухо"),
            wordEnRu("stomach", "живот"),
            wordEnRu("digestion", "пищеварение"),
            wordEnRu("toe", "палец на ноге"),
            wordEnRu("ankle", "лодыжка"),
            wordEnRu("screen", "экран"),
            wordEnRu("keyboard", "клавиатура"),
            wordEnRu("headphones", "наушники"),
            wordEnRu("microphone", "микрофон"),
            wordEnRu("laptop", "ноутбук"),
            wordEnRu("notebook", "записная книжка"),
            wordEnRu("spacious", "просторный"),
            wordEnRu("milk", "молоко"),
            wordEnRu("sour cream", "сметана"),
        ).map { DictionaryEntry(null, it) }.forEach { save(it) }
    }

    private fun wordEnRu(value: String, translation: String) = Word(value, translation, Language.EN, Language.RU)
}
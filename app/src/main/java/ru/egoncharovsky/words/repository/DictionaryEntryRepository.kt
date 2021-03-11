package ru.egoncharovsky.words.repository

import mu.KotlinLogging
import ru.egoncharovsky.words.domain.DictionaryEntry
import ru.egoncharovsky.words.domain.Word

object DictionaryEntryRepository : InMemoryRepository<Long, DictionaryEntry>(LongIdGenerator()) {

    private val logger = KotlinLogging.logger {}

    fun searchWord(value: String) = entities.values.filter {
        logger.debug("Search $value")
        it.word.value.contains(value, ignoreCase = true) || it.word.translation.contains(value, ignoreCase = true)
    }

    init {
        listOf(
            Word("apple", "яблоко"),
            Word("any", "любой"),
            Word("you", "ты"),
            Word("I", "я"),
            Word("many", "много"),
            Word("love", "любить"),
            Word("TV", "телевизор"),
            Word("shift", "сдвиг"),
            Word("weather", "погода"),
            Word("translation", "перевод"),
            Word("nice", "приятный"),
            Word("very", "очень"),
            Word("Greece", "Греция"),
            Word("cool", "круто"),
            Word("cold", "холодно"),
            Word("hot", "горячо"),
            Word("hungry", "голодный"),
            Word("breakfast", "завтрак"),
            Word("flower", "цветок"),
            Word("cub", "детеныш"),
            Word("pagan", "язычный"),
            Word("smart", "умный"),
            Word("prudent", "благоразумный"),
            Word("story", "история"),
            Word("mouse", "мышь"),
            Word("cup", "чашка"),
            Word("mag", "кружка"),
            Word("tea", "чай"),
            Word("box", "коробка"),
            Word("list", "список"),
            Word("blanket", "одеяло"),
            Word("eye", "глаз"),
            Word("ear", "ухо"),
            Word("stomach", "живот"),
            Word("digestion", "пищеварение"),
            Word("toe", "палец на ноге"),
            Word("ankle", "лодыжка"),
            Word("screen", "экран"),
            Word("keyboard", "клавиатура"),
            Word("headphones", "наушники"),
            Word("microphone", "микрофон"),
            Word("laptop", "ноутбук"),
            Word("notebook", "записная книжка"),
            Word("spacious", "просторный"),
            Word("milk", "молоко"),
            Word("sour cream", "сметана"),
        ).map { DictionaryEntry(null, it) }.forEach { save(it) }
    }
}
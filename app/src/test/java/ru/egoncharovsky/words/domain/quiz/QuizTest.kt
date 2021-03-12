package ru.egoncharovsky.words.domain.quiz

import ru.egoncharovsky.words.domain.Language
import ru.egoncharovsky.words.domain.Word

object QuizTest {
    val dictionary = listOf(
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
        wordEnRu("breakfast", "завтра"),
    )

    private fun wordEnRu(value: String, translation: String) = Word(value, translation, Language.EN, Language.RU)
}
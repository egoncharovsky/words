package ru.egoncharovsky.words.domain.quiz

import org.junit.Test

import ru.egoncharovsky.words.domain.Word

class QuizManagerTest {

    @Test
    fun start() {
        val words = listOf<Word>(
            Word("apple", "яблоко"),
            Word("any", "любой"),
            Word("you", "ты"),
            Word("I", "я"),
            Word("many", "много"),
            Word("love", "любить"),
            Word("TV", "телевизор"),
        )

        val manager = QuizManager(words)
        var i = 0

        do {
            i++
            print("call $i ")
            val word = manager.nextWord()
            println(word)
        } while (word != null)
    }
}
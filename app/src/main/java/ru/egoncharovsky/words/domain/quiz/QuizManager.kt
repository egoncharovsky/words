package ru.egoncharovsky.words.domain.quiz

import ru.egoncharovsky.words.domain.Word

class QuizManager(
    val words: List<Word>
) {
    val WINDOW_SIZE = 7
    val DISTANCE = 2

    val firstIteration = Iteration(3)
    val secondIteration = Iteration(5)

    private val wordProgresses = words.map { it to 0 }.toMap().toMutableMap()
    private val last: MutableList<Word> = mutableListOf()

    fun nextWord(): Word? {
        val nextWord = firstIteration.nextWord()
        return if (nextWord != null) {
            nextWord
        } else {
            print("second is used ")
            secondIteration.nextWord()
        }
    }

    inner class Iteration(
        val progressLimit: Int
    ) {
        private val rest: MutableList<Word>
        private val window: MutableList<Word>


        init {
            val shuffled = words.shuffled()
            window = shuffled.take(WINDOW_SIZE).toMutableList()
            rest = shuffled.minus(window).toMutableList()
        }

        fun nextWord(): Word? {
            if (window.size == 0) {
                return null
            }

            val word = if (window.size > DISTANCE) {
                window.minus(last).random()
            } else {
                window.random()
            }
            val progress = wordProgresses[word]!!

            val nextWord =
                if (progress < progressLimit) {
                    wordProgresses[word] = progress + 1
                    print("progress is $progress ")
                    word
                } else {
                    window.remove(word)

                    if (rest.size != 0) {
                        val newWord = rest.random()
                        window.add(newWord)
                        rest.remove(newWord)
                        newWord
                    } else {
                        nextWord()
                    }
                }

            if (last.size > DISTANCE) {
                last.removeFirst()
            }
            last.add(word)

            return nextWord
        }
    }
}
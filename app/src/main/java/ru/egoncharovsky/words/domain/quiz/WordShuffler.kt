package ru.egoncharovsky.words.domain.quiz

import mu.KotlinLogging
import ru.egoncharovsky.words.domain.Word

class WordShuffler(
    val words: List<Word>,
    private val windowSize: Int,
    private val progressLimit: Int,
    private val minDistance: Int = 0
) : Iterator<Word> {
    private val logger = KotlinLogging.logger { }

    private val progress = words.map { it to 0 }.toMap().toMutableMap()
    private val last: MutableList<Word> = mutableListOf()

    private val rest: MutableList<Word>
    private val window: MutableList<Word>

    init {
        require(windowSize > 0)
        require(progressLimit > 0)
        require(minDistance >= 0)
        require(minDistance < windowSize)

        val shuffled = words.shuffled()
        window = shuffled.take(windowSize).toMutableList()
        rest = shuffled.minus(window).toMutableList()
    }

    override fun hasNext(): Boolean = window.size != 0

    override fun next(): Word {
        logger.trace(
            """Getting next word with 
                |   rest:       $rest
                |   window:     $window
                |   last:       $last
                |   progress:   ${progress.map { it.key.value to it.value }}""".trimMargin()
        )

        val word = windowRandom()

        incrementLast(word)
        incrementProgress(word)

        if (progress[word]!! >= progressLimit) {
            logger.trace("Progress exceeded for $word")
            window.remove(word)
            if (rest.size != 0) {
                val newWord = rest.random()
                logger.trace("Replaced with $newWord")

                rest.remove(newWord)
                window.add(newWord)
            }
        }

        logger.debug("Return $word")
        return word
    }

    private fun incrementProgress(word: Word) {
        logger.trace("Increment progress of $word from ${progress[word]!!}")
        progress[word] = progress[word]!! + 1
    }

    private fun windowRandom(): Word {
        val tail = if (window.size > last.size) last else last.takeLast(window.size - 1)
        logger.trace("Tail is $tail")
        val forChoosing = window.minus(tail)
        val word = forChoosing.random()
        logger.trace("Random word is $word from $forChoosing")

        return word
    }

    private fun incrementLast(word: Word) {
        if (minDistance > 0) {
            if (last.size >= minDistance) {
                last.removeFirst()
            }
            last.add(word)
        }
    }
}
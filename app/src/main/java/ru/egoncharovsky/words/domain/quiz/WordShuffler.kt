package ru.egoncharovsky.words.domain.quiz

import mu.KotlinLogging
import ru.egoncharovsky.words.domain.Word

class WordShuffler(
    val words: Set<Word>,
    windowSize: Int,
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
        logger.trace("Getting next word with ${statistic()}")
        if (window.isEmpty()) throw NoSuchElementException("No words in window")

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

    fun decrementProgress(word: Word) {
        logger.trace("Decrement progress of $word from ${progress[word]!!}")
        if (!window.contains(word))
            logger.warn("Word $word is not in window ${statistic()}")

        if (progress[word]!! > 0) {
            progress[word] = progress[word]!! - 1
            logger.trace("New progress for $word is ${progress[word]}")
        }
    }

    private fun incrementProgress(word: Word) {
        logger.trace("Increment progress of $word from ${progress[word]!!}")
        if (!window.contains(word))
            logger.warn("Try to increment: $word is not in window ${statistic()}")
        if (progress[word]!! >= progressLimit)
            logger.warn("Try to increment: $word ${progress[word]!!} exceeds limit $progressLimit ${statistic()}")

        if (progress[word]!! < progressLimit) {
            progress[word] = progress[word]!! + 1
            logger.trace("New progress for $word is ${progress[word]}")
        }
    }

    fun progressOf(word: Word) = progress[word]!!

    fun returned() = last.toSet()

    private fun windowRandom(): Word {
        val tailSize = if (window.size > minDistance) minDistance else window.size - 1
        val tail = last.takeLast(tailSize)
        logger.trace("Tail size $tailSize and tail is $tail")

        val forChoosing = window.minus(tail)
        val word = forChoosing.random()
        logger.trace("Random word is $word from $forChoosing")

        return word
    }

    private fun incrementLast(word: Word) {
        if (minDistance > 0) {
            last.add(word)
        }
    }

    private fun statistic() = """
        |
        |   rest:       $rest
        |   window:     $window
        |   last:       $last
        |   progress:   ${progress.map { it.key.value to it.value }}""".trimMargin()
}
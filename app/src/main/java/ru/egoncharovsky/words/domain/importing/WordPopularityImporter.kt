package ru.egoncharovsky.words.domain.importing

import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import mu.KotlinLogging
import ru.egoncharovsky.words.repository.persistent.WordRepository
import java.io.InputStream

class WordPopularityImporter(
    private val wordRepository: WordRepository
) {

    private val logger = KotlinLogging.logger { }

    suspend fun upgradePopularity(statisticDictionaryIS: InputStream) {
            try {
                val statistic = loadStatistic(statisticDictionaryIS)
                logger.trace("Statistic size: ${statistic.size}")

                val words = wordRepository.getAll().take(1).single()

                val ratings = words.mapNotNull {
                    val rating = statistic[it.value]
                    if (rating != null) {
                        it.id!! to rating
                    } else {
                        null
                    }
                }
                    .toMap()
                logger.trace("Found ratings for ${ratings.size} words from ${words.size}")

                wordRepository.upgradePopularityRatings(ratings)
                logger.debug("Popularity ratings updated")
            } catch (e: Exception) {
                logger.error("Words popularity rating upgrade failed", e)
            }
        }

    private fun loadStatistic(statisticDictionaryIS: InputStream): Map<String, Int> {
        return statisticDictionaryIS.reader().useLines {
            it.map(this::readLine).toMap()
        }
    }

    private fun readLine(line: String): Pair<String, Int> {
        return line.split('\t').let {
            it[0].lowercase() to it[1].toInt()
        }
    }
}
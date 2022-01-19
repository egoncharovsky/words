package ru.egoncharovsky.words.domain.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import ru.egoncharovsky.words.domain.entity.Word
import ru.egoncharovsky.words.repository.persistent.WordRepository
import javax.inject.Singleton

@Singleton
class WordService(
    private val wordRepository: WordRepository
) {

    /**
     * @param wordsChosenIds chosen words ids
     * @return words not included in any [ru.egoncharovsky.words.domain.entity.StudyList]
     * and also chosen words
     */
    fun findHidingAlreadyIncluded(wordsChosenIds: Set<Long>): Flow<List<Word>> {
        return wordRepository.findNotIncludedInStudyLists()
            .zip(wordRepository.get(wordsChosenIds)) { list, elements ->
                list.plus(elements).toSet().toList()
            }
    }

    /**
     * @param wordsChosenIds chosen words ids
     * @return search in words not included in any [ru.egoncharovsky.words.domain.entity.StudyList]
     * and also chosen words
     */
    fun searchHidingAlreadyIncluded(text: String, wordsChosenIds: Set<Long>): Flow<List<Word>> {
        return wordRepository.searchNotIncludedInStudyLists(text)
            .zip(wordRepository.searchInWordsWithIds(text, wordsChosenIds)) { list, elements ->
                list.plus(elements).toSet().toList()
            }
    }
}
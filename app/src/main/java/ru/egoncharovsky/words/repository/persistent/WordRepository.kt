package ru.egoncharovsky.words.repository.persistent

import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.domain.entity.Word

interface WordRepository {
    fun getAll(): Flow<List<Word>>
    fun get(ids: Set<Long>): Flow<List<Word>>

    suspend fun saveImportedWords(words: List<Word>): List<Long>

    fun searchWord(value: String): Flow<List<Word>>

    fun findNotIncludedInStudyLists(): Flow<List<Word>>
    fun searchNotIncludedInStudyLists(value: String): Flow<List<Word>>
    fun searchInWordsWithIds(value: String, ids: Set<Long>): Flow<List<Word>>
    fun findWordsIdsIncludedInStudyListsExcluding(studyListId: Long? = null): Flow<Set<Long>>
}


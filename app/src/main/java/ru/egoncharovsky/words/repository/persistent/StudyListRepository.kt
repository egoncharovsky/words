package ru.egoncharovsky.words.repository.persistent

import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.domain.entity.StudyList

interface StudyListRepository {
    fun getAll(): Flow<List<StudyList>>
    fun get(id: Long): Flow<StudyList>
    fun save(studyList: StudyList)
    fun delete(id: Long)
}
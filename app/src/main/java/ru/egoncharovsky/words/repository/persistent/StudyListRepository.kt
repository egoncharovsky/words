package ru.egoncharovsky.words.repository.persistent

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mu.KotlinLogging
import ru.egoncharovsky.words.database.dao.StudyListDao
import ru.egoncharovsky.words.database.tables.StudyListWordJoin
import ru.egoncharovsky.words.domain.entity.StudyList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudyListRepository @Inject constructor(
    private val dao: StudyListDao
) {

    private val logger = KotlinLogging.logger { }

    fun getAll(): Flow<List<StudyList>> = dao.getAll().map { list -> list.map { it.toEntity() } }

    fun get(id: Long): Flow<StudyList> = dao.get(id).map { it.toEntity() }

    fun save(studyList: StudyList) {
        if (studyList.id == null) {
            logger.trace("Saving new $studyList")
            dao.insert(StudyListWordJoin.fromEntity(studyList))
        } else {
            logger.trace("Updating $studyList")
            dao.update(StudyListWordJoin.fromEntity(studyList))
        }
    }

    fun delete(id: Long) = dao.delete(id)
}
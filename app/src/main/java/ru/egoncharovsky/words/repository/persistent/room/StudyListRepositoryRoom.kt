package ru.egoncharovsky.words.repository.persistent.room

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mu.KotlinLogging
import ru.egoncharovsky.words.database.dao.StudyListDao
import ru.egoncharovsky.words.database.tables.StudyListTable.Companion.fromEntity
import ru.egoncharovsky.words.database.tables.StudyListWordCrossRef.Companion.toWordIds
import ru.egoncharovsky.words.domain.entity.StudyList
import ru.egoncharovsky.words.repository.persistent.StudyListRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudyListRepositoryRoom @Inject constructor(
    private val dao: StudyListDao
) : StudyListRepository {

    private val logger = KotlinLogging.logger { }

    override fun getAll(): Flow<List<StudyList>> = dao.getAll().map { list -> list.map { it.toEntity() } }

    override fun get(id: Long): Flow<StudyList> = dao.get(id).map { it.toEntity() }

    override fun save(studyList: StudyList) {
        if (studyList.id == null) {
            logger.trace("Saving new $studyList")
            dao.insert(fromEntity(studyList), toWordIds(studyList))
        } else {
            logger.trace("Updating $studyList")
            dao.update(fromEntity(studyList), toWordIds(studyList))
        }
    }

    override fun delete(id: Long) = dao.delete(id)
}
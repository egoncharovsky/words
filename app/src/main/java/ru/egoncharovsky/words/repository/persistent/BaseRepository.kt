package ru.egoncharovsky.words.repository.persistent

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.egoncharovsky.words.domain.entity.Identifiable
import ru.egoncharovsky.words.repository.AsyncRepository

abstract class BaseRepository<ID, E : Identifiable<E, ID>> : AsyncRepository<ID, E> {

    protected abstract suspend fun insert(entity: E): ID

    protected abstract suspend fun update(entity: E): Int

    override fun get(id: ID): Flow<E> = find(id).map { it!! }

    override suspend fun save(entity: E): ID {
        return if (entity.id == null) {
            insert(entity)
        } else {
            update(entity)
            entity.id!!
        }
    }
}
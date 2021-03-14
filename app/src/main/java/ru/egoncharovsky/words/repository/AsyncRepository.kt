package ru.egoncharovsky.words.repository

import kotlinx.coroutines.flow.Flow
import ru.egoncharovsky.words.domain.entity.Identifiable

interface AsyncRepository<ID, E : Identifiable<E, ID>> {

    fun getAll(): Flow<List<E>>

    fun get(id: ID): Flow<E>

    fun find(id: ID): Flow<E?>

    suspend fun save(entity: E): ID

    suspend fun saveAll(entities: Collection<E>): List<ID>

    suspend fun delete(entity: E)

    suspend fun deleteAll()
}
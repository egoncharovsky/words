package ru.egoncharovsky.words.repository

import ru.egoncharovsky.words.domain.entity.Entity

interface Repository<ID, E : Entity<E, ID>> {

    fun getAll(): List<E>

    fun get(id: ID): E

    fun find(id: ID): E?

    fun save(entity: E): E

    fun saveAll(entities: Iterable<E>): List<E>

    fun delete(entity: E)

    fun deleteAll()
}
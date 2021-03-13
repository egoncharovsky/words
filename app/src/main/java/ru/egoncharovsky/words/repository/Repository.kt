package ru.egoncharovsky.words.repository

import ru.egoncharovsky.words.domain.entity.Identifiable

interface Repository<ID, E : Identifiable<E, ID>> {

    fun getAll(): List<E>

    fun get(id: ID): E

    fun find(id: ID): E?

    fun save(entity: E): E

    fun saveAll(entities: Iterable<E>): List<E>

    fun delete(entity: E)

    fun deleteAll()
}
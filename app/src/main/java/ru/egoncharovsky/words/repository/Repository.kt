package ru.egoncharovsky.words.repository

import ru.egoncharovsky.words.domain.Entity

interface Repository<ID, E : Entity<ID>> {

    fun getAll(): List<E>

    fun get(id: ID): E

    fun find(id: ID): E?

    fun save(entity: E): E

    fun delete(entity: E)

    fun deleteAll()
}
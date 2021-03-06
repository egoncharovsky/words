package ru.egoncharovsky.words.repository

import ru.egoncharovsky.words.domain.Entity

interface Repository<ID, E : Entity<ID>> {

    fun getAll(): List<Entity<ID>>

    fun get(id: ID): Entity<ID>

    fun find(id: ID): Entity<ID>?

    fun add(entity: Entity<ID>): Entity<ID>

    fun delete(entity: Entity<ID>)

    fun deleteAll()
}
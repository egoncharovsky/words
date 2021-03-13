package ru.egoncharovsky.words.repository

import ru.egoncharovsky.words.domain.entity.Entity

open class InMemoryRepository<ID, E : Entity<E, ID>>(
    private val idGenerator: IdGenerator<ID>
) : Repository<ID, E> {

    interface IdGenerator<ID> {
        fun generate(): ID
    }

    class LongIdGenerator : IdGenerator<Long> {
        private val iterator = generateSequence(1L) { it + 1 }.iterator()

        override fun generate(): Long = iterator.next()
    }

    protected val entities = mutableMapOf<ID, E>()

    override fun getAll(): List<E> = entities.values.toList()

    override fun get(id: ID): E = entities[id]!!

    override fun find(id: ID): E? = entities[id]

    override fun save(entity: E): E {
        return if (entity.id == null) {
            entity.copy(idGenerator.generate())
        } else {
            entity
        }.also { entities[it.id!!] = it }
    }

    override fun saveAll(entities: Iterable<E>): List<E> = entities.map { save(it) }

    override fun delete(entity: E) {
        entity.id?.let { entities.remove(it) }
    }

    override fun deleteAll() {
        entities.clear()
    }
}
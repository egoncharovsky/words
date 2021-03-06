package ru.egoncharovsky.words.repository

import ru.egoncharovsky.words.domain.Entity

open class InMemoryRepository<ID, E : Entity<ID>>(
    private val idGenerator: IdGenerator<ID>
) : Repository<ID, E> {

    interface IdGenerator<ID> {
        fun generate(): ID
    }

    class LongIdGenerator : IdGenerator<Long> {
        private val sequence = generateSequence(0L) { it + 1 }

        override fun generate(): Long = sequence.iterator().next()
    }

    private val entities = mutableMapOf<ID, Entity<ID>>()

    override fun getAll(): List<Entity<ID>> = entities.values.toList()

    override fun get(id: ID): Entity<ID> = entities[id]!!

    override fun find(id: ID): Entity<ID>? = entities[id]

    override fun add(entity: Entity<ID>): Entity<ID> = entity.apply {
        val id = idGenerator.generate()
        this.id = id
        entities[id] = this
    }

    override fun delete(entity: Entity<ID>) { entity.id?.let { entities.remove(it) } }

    override fun deleteAll() { entities.clear() }
}
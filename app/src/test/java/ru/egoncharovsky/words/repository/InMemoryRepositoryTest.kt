package ru.egoncharovsky.words.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import ru.egoncharovsky.words.domain.entity.Entity

internal class InMemoryRepositoryTest {

    data class MyEntity(
        override var id: Long?
    ): Entity<Long>
    val repository = InMemoryRepository<Long, MyEntity>(InMemoryRepository.LongIdGenerator())

    @Test
    fun getAll() {
    }

    @Test
    fun get() {
    }

    @Test
    fun find() {
    }

    @Test
    fun add() {
        (1..3).map { MyEntity(null) }.forEach { repository.save(it) }

        val all = repository.getAll()

        assertEquals(3, all.size)
        all.forEach { assertNotNull(it.id) }
        assertEquals(3, all.map { it.id }.toSet().size)
    }

    @Test
    fun delete() {
    }

    @Test
    fun deleteAll() {
    }
}
package ru.egoncharovsky.words.domain.entity

interface Entity<out E, ID> where E: Entity<E, ID> {
    val id: ID?

    fun copy(newId: ID): E
}
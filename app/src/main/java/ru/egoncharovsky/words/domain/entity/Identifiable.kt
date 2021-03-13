package ru.egoncharovsky.words.domain.entity

interface Identifiable<out E, ID> where E: Identifiable<E, ID> {
    val id: ID?

    fun copy(newId: ID): E
}
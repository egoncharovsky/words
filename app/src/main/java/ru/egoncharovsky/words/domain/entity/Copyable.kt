package ru.egoncharovsky.words.domain.entity

interface Copyable<out E : Identifiable<E, ID>, ID>{
    fun copy(newId: ID): E
}
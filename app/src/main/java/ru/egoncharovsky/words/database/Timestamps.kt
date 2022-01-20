package ru.egoncharovsky.words.database

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

object Timestamps {

    fun fromMillis(value: Long): LocalDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC)

    fun toMillis(value: LocalDateTime) =
        value.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
}
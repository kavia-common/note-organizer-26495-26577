package com.oceanpro.notes.data

/**
 * PUBLIC_INTERFACE
 * Data model representing a Note entity.
 */
data class Note(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val updatedAt: Long = System.currentTimeMillis()
)

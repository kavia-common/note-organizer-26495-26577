package com.oceanpro.notes.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * Repository backed by SQLiteOpenHelper emitting state via StateFlow.
 */
class NotesRepositorySqlite(context: Context) {

    private val db = DatabaseHelper(context.applicationContext)
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private var lastQuery: String = ""

    init {
        refresh()
    }

    private fun refresh() {
        scope.launch {
            _notes.value = if (lastQuery.isBlank()) db.getAll() else db.search(lastQuery)
        }
    }

    /**
     * PUBLIC_INTERFACE
     * Update active search query and refresh results.
     */
    fun setQuery(query: String) {
        lastQuery = query.trim()
        refresh()
    }

    /**
     * PUBLIC_INTERFACE
     * Return current note list flow.
     */
    fun observe(): StateFlow<List<Note>> = notes

    /**
     * PUBLIC_INTERFACE
     * Get a note by id (suspend to run on IO dispatcher).
     */
    suspend fun getNote(id: Long): Note? = db.getById(id)

    /**
     * PUBLIC_INTERFACE
     * Insert or update a note then refresh the list.
     */
    suspend fun upsert(note: Note): Long {
        val id = db.upsert(note)
        refresh()
        return id
    }

    /**
     * PUBLIC_INTERFACE
     * Delete note and refresh the list.
     */
    suspend fun delete(id: Long) {
        db.delete(id)
        refresh()
    }
}

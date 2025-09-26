package com.oceanpro.notes.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.oceanpro.notes.data.Note
import com.oceanpro.notes.data.NotesRepositorySqlite
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * ViewModel for creating/updating a note.
 */
class NoteEditViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = NotesRepositorySqlite(app)

    /**
     * PUBLIC_INTERFACE
     * Save a note: if id is 0, creates, else updates.
     */
    fun save(id: Long, title: String, content: String, onSaved: (Long) -> Unit) {
        viewModelScope.launch {
            val note = if (id == 0L) Note(title = title, content = content)
            else Note(id = id, title = title, content = content)
            val newId = repo.upsert(note)
            onSaved(if (id == 0L) newId else id)
        }
    }

    /**
     * PUBLIC_INTERFACE
     * Load an existing note to prefill fields.
     */
    suspend fun load(id: Long): Note? {
        return repo.getNote(id)
    }
}

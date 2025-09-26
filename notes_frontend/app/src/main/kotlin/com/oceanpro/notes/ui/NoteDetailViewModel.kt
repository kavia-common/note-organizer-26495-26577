package com.oceanpro.notes.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.oceanpro.notes.data.Note
import com.oceanpro.notes.data.NotesRepositorySqlite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * ViewModel for note details screen.
 */
class NoteDetailViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = NotesRepositorySqlite(app)

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note

    /**
     * PUBLIC_INTERFACE
     * Load a note by id.
     */
    fun load(id: Long) {
        viewModelScope.launch {
            _note.value = repo.getNote(id)
        }
    }

    /**
     * PUBLIC_INTERFACE
     * Delete the current note by id.
     */
    fun delete(id: Long, onDone: () -> Unit) {
        viewModelScope.launch {
            repo.delete(id)
            onDone()
        }
    }
}

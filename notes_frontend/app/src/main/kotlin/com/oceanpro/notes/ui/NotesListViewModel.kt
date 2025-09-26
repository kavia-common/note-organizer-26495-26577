package com.oceanpro.notes.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.oceanpro.notes.data.NotesRepositorySqlite
import com.oceanpro.notes.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * ViewModel for the notes list screen handling search and observation.
 */
class NotesListViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = NotesRepositorySqlite(app)

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    init {
        observeNotes()
    }

    private fun observeNotes() {
        viewModelScope.launch {
            repo.observe().collectLatest { _notes.value = it }
        }
        viewModelScope.launch {
            _query.collectLatest { q -> repo.setQuery(q) }
        }
    }

    /**
     * PUBLIC_INTERFACE
     * Update current search query.
     */
    fun setQuery(q: String) {
        _query.value = q
    }
}

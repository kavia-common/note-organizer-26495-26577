package com.oceanpro.notes.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.oceanpro.notes.R
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * Screen for creating or editing a note.
 */
class NoteEditActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_edit_note_id"
    }

    private val vm: NoteEditViewModel by viewModels()
    private var noteId: Long = 0L

    private lateinit var toolbar: Toolbar
    private lateinit var etTitle: TextInputEditText
    private lateinit var etContent: TextInputEditText
    private lateinit var btnCancel: MaterialButton
    private lateinit var btnSave: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        btnCancel = findViewById(R.id.btnCancel)
        btnSave = findViewById(R.id.btnSave)

        noteId = intent.getLongExtra(EXTRA_ID, 0L)
        toolbar.title = if (noteId == 0L) getString(R.string.add_note) else getString(R.string.edit_note)

        if (noteId != 0L) {
            lifecycleScope.launch {
                vm.load(noteId)?.let { note ->
                    etTitle.setText(note.title)
                    etContent.setText(note.content)
                }
            }
        }

        btnCancel.setOnClickListener { finish() }
        btnSave.setOnClickListener {
            val title = etTitle.text?.toString()?.trim().orEmpty()
            val content = etContent.text?.toString()?.trim().orEmpty()
            vm.save(noteId, title, content) {
                finish()
            }
        }
    }
}

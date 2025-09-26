package com.oceanpro.notes.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.oceanpro.notes.R
import com.oceanpro.notes.util.Formatters
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * Displays a single note with options to edit or delete.
 */
class NoteDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_note_id"
    }

    private val vm: NoteDetailViewModel by viewModels()
    private var noteId: Long = 0L

    private lateinit var toolbar: Toolbar
    private lateinit var tvTitle: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvContent: TextView
    private lateinit var btnEdit: MaterialButton
    private lateinit var btnDelete: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        tvTitle = findViewById(R.id.tvTitle)
        tvDate = findViewById(R.id.tvDate)
        tvContent = findViewById(R.id.tvContent)
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)

        noteId = intent.getLongExtra(EXTRA_ID, 0L)
        vm.load(noteId)

        lifecycleScope.launch {
            vm.note.collectLatest { note ->
                if (note != null) {
                    toolbar.title = note.title.ifBlank { getString(R.string.notes) }
                    tvTitle.text = note.title.ifBlank { "(Untitled)" }
                    tvContent.text = note.content
                    tvDate.text = Formatters.formatDate(note.updatedAt)
                }
            }
        }

        btnEdit.setOnClickListener {
            val i = Intent(this, NoteEditActivity::class.java)
            i.putExtra(NoteEditActivity.EXTRA_ID, noteId)
            startActivity(i)
        }

        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.yes) { _, _ ->
                    vm.delete(noteId) { finish() }
                }
                .setNegativeButton(R.string.no, null)
                .show()
        }
    }
}

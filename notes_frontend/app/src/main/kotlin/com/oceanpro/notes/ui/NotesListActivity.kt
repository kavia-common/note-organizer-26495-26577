package com.oceanpro.notes.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.oceanpro.notes.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * Main screen showing searchable list of notes with add action.
 */
class NotesListActivity : AppCompatActivity() {

    private val vm: NotesListViewModel by viewModels()

    private lateinit var toolbar: Toolbar
    private lateinit var rvNotes: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var etSearch: EditText
    private lateinit var fabAdd: FloatingActionButton

    private val adapter = NotesAdapter(onClick = { note ->
        val i = Intent(this, NoteDetailActivity::class.java)
        i.putExtra(NoteDetailActivity.EXTRA_ID, note.id)
        startActivity(i)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        rvNotes = findViewById(R.id.rvNotes)
        tvEmpty = findViewById(R.id.tvEmpty)
        etSearch = findViewById(R.id.etSearch)
        fabAdd = findViewById(R.id.fabAdd)

        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.adapter = adapter

        fabAdd.setOnClickListener {
            startActivity(Intent(this, NoteEditActivity::class.java))
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.setQuery(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        lifecycleScope.launch {
            vm.notes.collectLatest { list ->
                adapter.submit(list)
                tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}

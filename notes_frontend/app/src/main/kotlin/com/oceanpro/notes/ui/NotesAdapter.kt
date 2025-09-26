package com.oceanpro.notes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oceanpro.notes.R
import com.oceanpro.notes.data.Note
import com.oceanpro.notes.util.Formatters
import android.widget.TextView
import com.google.android.material.card.MaterialCardView

/**
 * PUBLIC_INTERFACE
 * Adapter for displaying the list of notes.
 */
class NotesAdapter(
    private var items: List<Note> = emptyList(),
    private val onClick: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val cardRoot: MaterialCardView = itemView.findViewById(R.id.cardRoot)

        fun bind(note: Note) {
            tvTitle.text = note.title.ifBlank { "(Untitled)" }
            tvContent.text = note.content
            tvDate.text = Formatters.formatDate(note.updatedAt)
            cardRoot.setOnClickListener { onClick(note) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_note, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    /**
     * PUBLIC_INTERFACE
     * Submit a new list to display.
     */
    fun submit(newItems: List<Note>) {
        items = newItems
        notifyDataSetChanged()
    }
}

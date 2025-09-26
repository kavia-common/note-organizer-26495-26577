package com.oceanpro.notes.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * PUBLIC_INTERFACE
 * Lightweight SQLiteOpenHelper for storing notes without annotation processing.
 */
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "ocean_notes.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "content TEXT NOT NULL," +
                "updatedAt INTEGER NOT NULL" +
            ")"
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_notes_updatedAt ON notes(updatedAt)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS notes")
        onCreate(db)
    }

    /**
     * PUBLIC_INTERFACE
     * Fetch all notes ordered by updatedAt DESC.
     */
    fun getAll(): List<Note> {
        val list = mutableListOf<Note>()
        val c: Cursor = readableDatabase.query(
            "notes", arrayOf("id","title","content","updatedAt"),
            null, null, null, null, "updatedAt DESC"
        )
        c.use {
            while (it.moveToNext()) {
                list.add(
                    Note(
                        id = it.getLong(0),
                        title = it.getString(1),
                        content = it.getString(2),
                        updatedAt = it.getLong(3)
                    )
                )
            }
        }
        return list
    }

    /**
     * PUBLIC_INTERFACE
     * Search notes by title or content.
     */
    fun search(query: String): List<Note> {
        val list = mutableListOf<Note>()
        val args = arrayOf("%$query%", "%$query%")
        val c: Cursor = readableDatabase.query(
            "notes", arrayOf("id","title","content","updatedAt"),
            "title LIKE ? OR content LIKE ?", args, null, null, "updatedAt DESC"
        )
        c.use {
            while (it.moveToNext()) {
                list.add(
                    Note(
                        id = it.getLong(0),
                        title = it.getString(1),
                        content = it.getString(2),
                        updatedAt = it.getLong(3)
                    )
                )
            }
        }
        return list
    }

    /**
     * PUBLIC_INTERFACE
     * Get a note by id.
     */
    fun getById(id: Long): Note? {
        val c = readableDatabase.query(
            "notes", arrayOf("id","title","content","updatedAt"),
            "id=?", arrayOf(id.toString()), null, null, null, "1"
        )
        c.use {
            if (it.moveToFirst()) {
                return Note(
                    id = it.getLong(0),
                    title = it.getString(1),
                    content = it.getString(2),
                    updatedAt = it.getLong(3)
                )
            }
        }
        return null
    }

    /**
     * PUBLIC_INTERFACE
     * Insert or update a note. Returns new id for insert, or existing id for update.
     */
    fun upsert(note: Note): Long {
        val values = ContentValues().apply {
            put("title", note.title)
            put("content", note.content)
            put("updatedAt", System.currentTimeMillis())
        }
        return if (note.id == 0L) {
            writableDatabase.insert("notes", null, values)
        } else {
            writableDatabase.update("notes", values, "id=?", arrayOf(note.id.toString()))
            note.id
        }
    }

    /**
     * PUBLIC_INTERFACE
     * Delete a note by id.
     */
    fun delete(id: Long) {
        writableDatabase.delete("notes", "id=?", arrayOf(id.toString()))
    }
}

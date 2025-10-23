package com.example.noteapp.data

import android.content.Context
import com.example.noteapp.Note
import com.example.noteapp.NotesDatabaseHelper

class NotesRepository(context: Context) {

    private val dbHelper = NotesDatabaseHelper(context)

    fun getNotesForUser(userId: String): List<Note> {
        return dbHelper.getAllNotes(userId)
    }

    fun insertNote(note: Note) {
        dbHelper.insertNote(note)
    }

    fun updateNote(note: Note) {
        dbHelper.updateNote(note)
    }

    fun deleteNote(noteId: Int) {
        dbHelper.deleteNote(noteId)
    }

    fun getNoteById(noteId: Int, userId: String): Note {
        return dbHelper.getNoteByID(noteId, userId)
    }
}

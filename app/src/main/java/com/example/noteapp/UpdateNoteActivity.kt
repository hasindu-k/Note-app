package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.noteapp.data.NotesRepository
import com.example.noteapp.databinding.ActivityUpdateNoteBinding

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var repository: NotesRepository
    private var noteId: Int = -1
    private lateinit var userId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = NotesRepository(this)

        noteId = intent.getIntExtra("note_id",-1)
        userId = intent.getStringExtra("user_id") ?: ""
        if(noteId == -1 || userId.isEmpty()){
            finish()
            return
        }

        val note = repository.getNoteById(noteId, userId)
        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditText.setText(note.content)

        binding.updateSaveButton.setOnClickListener{
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()

            if (newTitle.isEmpty() && newContent.isEmpty()) {
                Toast.makeText(this,"Enter both Title and Content",Toast.LENGTH_SHORT).show()
            }else{
                val updatedNote = Note(noteId, newTitle, newContent, userId)
                repository.updateNote(updatedNote)
                finish()
                Toast.makeText(this,"Changes Saved..!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
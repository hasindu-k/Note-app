package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.noteapp.databinding.ActivityAddNoteBinding
import com.google.firebase.auth.FirebaseAuth

class AddNoteActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityAddNoteBinding
    private lateinit var db: NotesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            if (title.isEmpty() && content.isEmpty()) {
                Toast.makeText(this,"Enter both Title and Content",Toast.LENGTH_SHORT).show()
            }else {

                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    val userId = currentUser.uid
                    val note = Note(0, title, content, userId)
                    db.insertNote(note)
                    finish()
                    Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}
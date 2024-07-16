package com.example.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val userName = currentUser.displayName

            binding.userNameTextView.text = userName ?: "Guest"

            db = NotesDatabaseHelper(this)
            notesAdapter = NotesAdapter(db.getAllNotes(userId), this, userId)

            // Display userNotes in your UI
            binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.notesRecyclerView.adapter = notesAdapter

            binding.addButton.setOnClickListener {
                val intent = Intent(this, AddNoteActivity::class.java)
                startActivity(intent)
            }
            binding.logoutButton.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            // Redirect to login screen
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            notesAdapter.refreshData(db.getAllNotes(userId))
        }

    }
}
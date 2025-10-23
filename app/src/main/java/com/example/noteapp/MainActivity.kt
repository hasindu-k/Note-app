package com.example.noteapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.data.NotesRepository
import com.example.noteapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var repository: NotesRepository
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = NotesRepository(this)

        val currentUser = auth.currentUser
        if (currentUser == null) {
            showToast("User not logged in")
            navigateToLogin()
            return
        }

        initUI(currentUser.email ?: "Guest", currentUser.uid)
    }

    override fun onResume() {
        super.onResume()
        refreshNotes()
    }

    /** ---------------- Helper Methods ---------------- */

    private fun initUI(userName: String, userId: String) {
        binding.userNameTextView.text = userName
        setupRecyclerView(userId)
        setupListeners(userId)
    }

    private fun setupRecyclerView(userId: String) {
        val notes = repository.getNotesForUser(userId)
        notesAdapter = NotesAdapter(notes, this, userId)
        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notesAdapter
        }
    }

    private fun setupListeners(userId: String) {
        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            navigateToLogin()
        }
    }

    private fun refreshNotes() {
        auth.currentUser?.let {
            val updatedNotes = repository.getNotesForUser(it.uid)
            notesAdapter.refreshData(updatedNotes)
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LogInActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

package com.example.mindfizz

import LeaderboardAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class LeaderboardActivity : ComponentActivity() {

    private lateinit var quizCode:String
    private val leadboardRef: DatabaseReference = DatabaseUtils.database.getReference("leadboard")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leaderboard)
        quizCode =intent.getStringExtra("quizCode") ?: "q1"
        loadLeaderboardAndListen(quizCode)


        val btHome = findViewById<Button>(R.id.btHome)

        btHome.setOnClickListener {
            // Navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finish the current activity to prevent going back to it
        }

    }

    private fun loadLeaderboardAndListen(quizCode: String) {
        DatabaseUtils.loadLeadboardByKey(quizCode, object : DatabaseUtils.BoardLoadListener {
            override fun onBoardLoaded(board: MutableList<LeaderboardEntry>) {
                Log.d("Leaderboard", board.toString())
                board.sortByDescending { it.score }
                val recyclerView: RecyclerView = findViewById(R.id.rwItems)
                recyclerView.layoutManager = LinearLayoutManager(this@LeaderboardActivity)
                recyclerView.adapter = LeaderboardAdapter(board)
            }

            override fun onBoardNotFound(quizCode: String) {
                TODO("Not yet implemented")
            }

            override fun onDatabaseError(error: String) {
                Log.e("Leaderboard", "Error loading leaderboard: $error")
            }
        })

        // Add a listener to the leaderboard reference to listen for changes
        leadboardRef.child(quizCode).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // When data changes, reload the leaderboard
                DatabaseUtils.loadLeadboardByKey(quizCode, object : DatabaseUtils.BoardLoadListener {
                    override fun onBoardLoaded(board: MutableList<LeaderboardEntry>) {
                        Log.d("Leaderboard", board.toString())
                        board.sortByDescending { it.score }
                        val recyclerView: RecyclerView = findViewById(R.id.rwItems)
                        recyclerView.layoutManager = LinearLayoutManager(this@LeaderboardActivity)
                        recyclerView.adapter = LeaderboardAdapter(board)
                    }

                    override fun onBoardNotFound(quizCode: String) {
                        TODO("Not yet implemented")
                    }

                    override fun onDatabaseError(error: String) {
                        Log.e("Leaderboard", "Error loading leaderboard: $error")
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Leaderboard", "Error listening for leaderboard changes: ${databaseError.message}")
            }
        })
    }

    override fun onBackPressed() {
        // Handle back button press
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finish the current activity to prevent going back to it
    }
}
package com.example.quiztesting

import LeaderboardAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LeaderboardActivity : ComponentActivity() {

    private lateinit var quizCode:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leaderboard)


        quizCode =intent.getStringExtra("quizCode") ?: "q1"
        DatabaseUtils.loadLeadboardByKey(quizCode,object : DatabaseUtils.BoardLoadListener {
            override fun onBoardLoaded(board: MutableList<LeaderboardEntry>) {
                Log.d("Leaderboard",board.toString())
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
}
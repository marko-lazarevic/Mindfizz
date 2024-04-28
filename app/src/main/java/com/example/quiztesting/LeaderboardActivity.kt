package com.example.quiztesting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class LeaderboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leaderboard)
        DatabaseUtils.loadLeadboardByKey("-NwYtMGPwhO2GFQ_jaxX",object : DatabaseUtils.BoardLoadListener {
            override fun onBoardLoaded(board: MutableList<LeaderboardEntry>) {
                Log.d("Leaderboard",board.toString())
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
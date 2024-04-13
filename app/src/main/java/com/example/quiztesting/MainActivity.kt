package com.example.quiztesting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class MainActivity : ComponentActivity() {
    private lateinit var etQuizCode: EditText
    private lateinit var database: FirebaseDatabase
    private lateinit var quizzesRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance()
        quizzesRef = database.getReference("quizzes")

        // Find views by their IDs
        etQuizCode = findViewById(R.id.etQuizCode)

        val btPlay = findViewById<Button>(R.id.btPlay)
        btPlay.setOnClickListener {
            val quizCode = etQuizCode.text.toString().trim()
            if (quizCode.isNotEmpty()) {
                loadQuizByKey(quizCode)
            } else {
                // Handle empty quiz code input
                // You can show an error message or take other actions as needed
            }
        }
    }

    private fun loadQuizByKey(quizCode: String) {
        DatabaseUtils.loadQuizByKey(quizCode, object : DatabaseUtils.QuizLoadListener {
            override fun onQuizLoaded(quiz: Quiz) {
                // Quiz loaded successfully, start QuizActivity or perform other actions
                val intent = Intent(this@MainActivity, QuizActivity::class.java)
                intent.putExtra("quizObject", quiz)
                startActivity(intent)
            }

            override fun onQuizNotFound(quizCode: String) {
                //TODO Handle quiz not found case
                Log.d("MainActivity", "Quiz not found for code: $quizCode")
            }

            override fun onDatabaseError(error: String) {
                //TODO Handle database error
                Log.e("MainActivity", "Error loading quiz: $error")
            }
        })

    }
}

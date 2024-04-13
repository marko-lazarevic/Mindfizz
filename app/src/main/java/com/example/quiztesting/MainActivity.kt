package com.example.quiztesting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.quiztesting.ui.theme.QuizTestingTheme
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
        quizzesRef.child(quizCode).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val quiz = dataSnapshot.getValue(Quiz::class.java)
                    Log.d("Main",quiz.toString())
                    if (quiz != null) {
                        // Quiz found, do something with it
                        // Example: Display the quiz details
                        Log.d("MainActivity", "Quiz found: ${quiz.name}")
                        // You can replace Log.d with code to display the quiz details
                        //TODO
                        //val intent = Intent(this@MainActivity, QuizActivity::class.java)
                        //intent.putExtra("quizObject", quiz)
                        //startActivity(intent)
                    }
                } else {
                    // Quiz not found
                    Log.d("MainActivity", "Quiz not found for code: $quizCode")
                    // You can replace Log.d with code to handle the quiz not found case
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Log.e("MainActivity", "Error loading quiz: ${databaseError.message}")
                // You can replace Log.e with code to handle the database error
            }
        })
    }
}

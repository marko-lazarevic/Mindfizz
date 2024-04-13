package com.example.quiztesting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
class QuizActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)

        if (intent.hasExtra("quizObject")) {
            // Retrieve the Quiz object from the intent extras
            val quiz = intent.getParcelableExtra<Quiz>("quizObject")

            // Now you can use the quiz object as needed
            if (quiz != null) {
                // Example: Log the quiz name
                Log.d("QuizActivity", "Quiz Name: ${quiz.name}")
            }
        } else {
            Log.e("QuizActivity", "Intent does not have 'quizObject' extra.")
        }
    }
}
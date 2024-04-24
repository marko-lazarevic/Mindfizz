package com.example.quiztesting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class QuizCreatedActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_created)

        val twCode = findViewById<TextView>(R.id.twCode)
        val btHome = findViewById<Button>(R.id.btHome)

        // Get the quiz code from extras
        val quizCode = intent.getStringExtra("quiz_code")
        twCode.text = quizCode

        // Set click listener to copy the quiz code to clipboard
        twCode.setOnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Quiz Code", quizCode)
            clipboardManager.setPrimaryClip(clip)
            // Show a message indicating the code is copied
            // You can use Toast or Snackbar for this
        }

        // Set click listener to share the quiz code
        twCode.setOnLongClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, quizCode)
                type = "text/plain"
            }
            startActivity(sendIntent)
            true
        }

        // Set click listener for btHome button
        btHome.setOnClickListener {
            // Navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finish the current activity to prevent going back to it
        }
    }

    override fun onBackPressed() {
        // Handle back button press
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finish the current activity to prevent going back to it
    }
}
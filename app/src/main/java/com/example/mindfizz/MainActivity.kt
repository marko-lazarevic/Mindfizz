package com.example.mindfizz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import android.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : ComponentActivity() {
    private lateinit var etQuizCode: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val auth = FirebaseAuth.getInstance()
        // Check if user is already signed in anonymously
        if (auth.currentUser == null) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Enter Display Name")

            // Set up the input field
            val input = EditText(this)
            builder.setView(input)

            // Set up the OK button
            builder.setPositiveButton("OK", null) // Set listener to null for now

            // Disable the dialog cancel button
            builder.setCancelable(false)

            val alertDialog = builder.create()

            // Override the OnShowListener to handle the button click
            alertDialog.setOnShowListener {
                val button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                button.setOnClickListener {
                    val displayName = input.text.toString().trim()
                    if (displayName.isNotEmpty()) {
                        // Use DatabaseUtils to create user and set display name
                        DatabaseUtils.createUser(displayName, object : DatabaseUtils.CreateUserListener {
                            override fun onUserCreated(user: FirebaseUser) {
                                // User created successfully, continue with app logic
                                alertDialog.dismiss() // Close the dialog after successful user creation
                            }

                            override fun onUserCreationFailed(error: String) {
                                // Handle user creation failure
                                Log.e("MainActivity", "User creation failed: $error")
                                // You can show an error message or take other actions as needed
                            }
                        })
                    } else {
                        // Wait for the user to enter a display name
                        input.error = "Please enter a display name"
                        input.requestFocus()
                    }
                }
            }

            alertDialog.show()
        } else {
            Log.d("MainActivity", auth.currentUser?.displayName.toString())
        }

        // Find views by their IDs
        etQuizCode = findViewById(R.id.etQuizCode)

        val btPlay = findViewById<Button>(R.id.btPlay)
        btPlay.setOnClickListener {
            val quizCode = etQuizCode.text.toString().trim()
            if (quizCode.isNotEmpty()) {
                loadQuizByKey(quizCode)
            } else {
                showAlertDialog()
            }
        }

        val btLeaderboard = findViewById<Button>(R.id.btLeaderboard)

        btLeaderboard.setOnClickListener{
            val quizCode = etQuizCode.text.toString().trim()
            if (quizCode.isNotEmpty()) {
                showLeaderboard(quizCode)
            } else {
                showAlertDialog()
            }
        }

        val btCreate = findViewById<Button>(R.id.btCreate)
        btCreate.setOnClickListener {
            val intent = Intent(this@MainActivity, CreateQuizActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadQuizByKey(quizCode: String) {
        DatabaseUtils.loadQuizByKey(quizCode, object : DatabaseUtils.QuizLoadListener {
            override fun onQuizLoaded(quiz: Quiz) {
                // Quiz loaded successfully, start QuizActivity or perform other actions
                val intent = Intent(this@MainActivity, QuizActivity::class.java)
                intent.putExtra("quiz", quiz)
                intent.putExtra("quizCode",quizCode)
                startActivity(intent)
            }

            override fun onQuizNotFound(quizCode: String) {
                showAlertDialog2("Quiz not found!")
                Log.d("MainActivity", "Quiz not found for code: $quizCode")
            }

            override fun onDatabaseError(error: String) {
                Log.e("MainActivity", "Error loading quiz: $error")
            }
        })

    }

    private fun showLeaderboard(quizCode: String){
        DatabaseUtils.loadLeadboardByKey(quizCode, object : DatabaseUtils.BoardLoadListener {
            override fun onBoardLoaded(board: MutableList<LeaderboardEntry>) {
                val intent = Intent(this@MainActivity, LeaderboardActivity::class.java)
                intent.putExtra("quizCode",quizCode)
                startActivity(intent)
            }

            override fun onBoardNotFound(quizCode: String) {
                showAlertDialog2("Leaderboard not found for that quiz code!")
                Log.d("MainActivity", "Quiz not found for code: $quizCode")
            }

            override fun onDatabaseError(error: String) {
                Log.e("Leaderboard", "Error loading leaderboard: $error")
            }
        })
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Quiz code empty")
            .setMessage("Please enter the quiz code.")
            .setPositiveButton("OK", null)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun showAlertDialog2(title:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage("Please try again.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                etQuizCode.setText("")
            }


        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()

    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Close App")
        builder.setMessage("Are you sure you want to close the app?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            moveTaskToBack(true)
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

}


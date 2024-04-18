package com.example.quiztesting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.app.AlertDialog
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest


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
                showAlertDialog2()
                Log.d("MainActivity", "Quiz not found for code: $quizCode")
            }

            override fun onDatabaseError(error: String) {
                Log.e("MainActivity", "Error loading quiz: $error")
            }
        })

    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Niste uneli kviz ID")
            .setMessage("Zelite li da unesete kviz ID?")
            .setPositiveButton("Da", null)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun showAlertDialog2() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nije pronadjen kviz")
            .setMessage("Unesite novi ID")
            .setPositiveButton("OK",) { dialog, which ->
                dialog.dismiss()
                etQuizCode.setText("")
            }


        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()

    }

}


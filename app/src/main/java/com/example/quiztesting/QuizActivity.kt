package com.example.quiztesting

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class QuizActivity: ComponentActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var option1TextView: TextView
    private lateinit var option2TextView: TextView
    private lateinit var option3TextView: TextView
    private lateinit var option4TextView: TextView

    // Define more text views for other options if needed
    private var currentQuestionIndex = 0
    private lateinit var quiz: Quiz
    private var score = 0
    private var selectedAnswer = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)

        questionTextView = findViewById(R.id.tvQuestion)
        option1TextView = findViewById(R.id.Ans1)
        option2TextView = findViewById(R.id.Ans2)
        option3TextView = findViewById(R.id.Ans3)
        option4TextView = findViewById(R.id.Ans4)


        // Extract Quiz object from intent extra
        quiz = intent.getParcelableExtra("quiz") ?: return
        // i pitanja da budu parcelable
        // tajmer 20s kad istekne sledece pitanja
        var totalQuestion: Int = quiz.questions.size
        displayCurrentQuestion()
    }

    private fun displayCurrentQuestion() {
        val questions = quiz.questions
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            val optionsForQuestion = question.answers

            questionTextView.text = question.question
            option1TextView.text = optionsForQuestion[0]
            option2TextView.text = optionsForQuestion[1]
            option3TextView.text = optionsForQuestion[2]
            option4TextView.text = optionsForQuestion[3]

        } else {
            // TODO
            // Quiz completed
        }
    }


    private fun onClick(view: View) {
        if (view !is Button) return // Ensure only buttons can trigger this method

        val clickedButton = view
        if (clickedButton.id == R.id.btnSubmit) {
            val currentQuestion = quiz.questions[currentQuestionIndex]
            val correctAnswerIndex = currentQuestion.correctAnswer

            if (selectedAnswer.isNotBlank() && correctAnswerIndex >= 0 && selectedAnswer.toIntOrNull() == correctAnswerIndex) {
                // Correct answer
                score++
                // Optionally provide feedback to the user

            } else {
                // TODO
                // Incorrect answer
                // Optionally provide feedback to the user

            }

            currentQuestionIndex++
            displayCurrentQuestion()
        } else {
            // Choices button clicked
            selectedAnswer = clickedButton.text.toString()
        }
    }
    fun onNextButtonClick(view: View) {
        currentQuestionIndex++
        displayCurrentQuestion()
    }

}
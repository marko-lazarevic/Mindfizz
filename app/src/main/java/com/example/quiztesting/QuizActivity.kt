package com.example.quiztesting

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
    private val answerOrder = IntArray(4)
    private var correctAnswerIndex = -1
    private var selectedAnswerIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)

        questionTextView = findViewById(R.id.tvQuestion)
        option1TextView = findViewById(R.id.Ans1)
        option2TextView = findViewById(R.id.Ans2)
        option3TextView = findViewById(R.id.Ans3)
        option4TextView = findViewById(R.id.Ans4)

        option1TextView.setOnClickListener { selectOption(0) }
        option2TextView.setOnClickListener { selectOption(1) }
        option3TextView.setOnClickListener { selectOption(2) }
        option4TextView.setOnClickListener { selectOption(3) }

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        // Extract Quiz object from intent extra
        quiz = intent.getParcelableExtra("quiz") ?: return

        btnSubmit.setOnClickListener {
            checkAnswer()
        }

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

            val optionsShuffled = optionsForQuestion.shuffled()

            answerOrder[0] = optionsShuffled.indexOf(optionsForQuestion[0])
            answerOrder[1] = optionsShuffled.indexOf(optionsForQuestion[1])
            answerOrder[2] = optionsShuffled.indexOf(optionsForQuestion[2])
            answerOrder[3] = optionsShuffled.indexOf(optionsForQuestion[3])

            correctAnswerIndex = optionsShuffled.indexOf(optionsForQuestion[question.correctAnswer])

            questionTextView.text = question.question
            option1TextView.text = optionsShuffled[0]
            option2TextView.text = optionsShuffled[1]
            option3TextView.text = optionsShuffled[2]
            option4TextView.text = optionsShuffled[3]

        } else {
            // TODO
            // Quiz completed
        }
    }

    private fun selectOption(index: Int) {
        // Reset border colors
        option1TextView.setBackgroundResource(android.R.color.transparent)
        option2TextView.setBackgroundResource(android.R.color.transparent)
        option3TextView.setBackgroundResource(android.R.color.transparent)
        option4TextView.setBackgroundResource(android.R.color.transparent)

        // Set selected border color
        when (index) {
            0 -> option1TextView.setBackgroundResource(R.drawable.border_black)
            1 -> option2TextView.setBackgroundResource(R.drawable.border_black)
            2 -> option3TextView.setBackgroundResource(R.drawable.border_black)
            3 -> option4TextView.setBackgroundResource(R.drawable.border_black)
        }

        // Update selected answer index
        selectedAnswerIndex = index
    }


    private fun checkAnswer(){
        val currentQuestion = quiz.questions[currentQuestionIndex]
        val correctAnswerIndex = currentQuestion.correctAnswer

        // Reset border colors
        option1TextView.setBackgroundResource(android.R.color.transparent)
        option2TextView.setBackgroundResource(android.R.color.transparent)
        option3TextView.setBackgroundResource(android.R.color.transparent)
        option4TextView.setBackgroundResource(android.R.color.transparent)

        // Color the selected answer
        when (selectedAnswerIndex) {
            0 -> option1TextView.setBackgroundResource(
                if (selectedAnswerIndex == correctAnswerIndex) R.drawable.border_green
                else R.drawable.border_red
            )
            1 -> option2TextView.setBackgroundResource(
                if (selectedAnswerIndex == correctAnswerIndex) R.drawable.border_green
                else R.drawable.border_red
            )
            2 -> option3TextView.setBackgroundResource(
                if (selectedAnswerIndex == correctAnswerIndex) R.drawable.border_green
                else R.drawable.border_red
            )
            3 -> option4TextView.setBackgroundResource(
                if (selectedAnswerIndex == correctAnswerIndex) R.drawable.border_green
                else R.drawable.border_red
            )
        }

        if (selectedAnswer.isNotBlank() && correctAnswerIndex >= 0 && selectedAnswer.toIntOrNull() == correctAnswerIndex) {
            // Correct answer
            score++
            // Optionally provide feedback to the user

        } else {
            // Incorrect answer
            // Optionally provide feedback to the user

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
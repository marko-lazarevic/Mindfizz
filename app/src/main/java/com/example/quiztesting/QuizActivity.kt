package com.example.quiztesting

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
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
    private lateinit var tvTimer: TextView
    private lateinit var btnSubmit:Button

    // Define more text views for other options if needed
    private var currentQuestionIndex = -1
    private lateinit var quiz: Quiz
    private lateinit var quizCode:String
    private var score = 0
    private val answerOrder = IntArray(4)
    private var correctAnswerIndex = -1
    private var selectedAnswerIndex = -1
    private lateinit var countDownTimer: CountDownTimer
    private var milisUntilFinish = 20100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)

        questionTextView = findViewById(R.id.tvQuestion)
        option1TextView = findViewById(R.id.Ans1)
        option2TextView = findViewById(R.id.Ans2)
        option3TextView = findViewById(R.id.Ans3)
        option4TextView = findViewById(R.id.Ans4)
        tvTimer = findViewById(R.id.tvTimer)

        option1TextView.setOnClickListener { selectOption(0) }
        option2TextView.setOnClickListener { selectOption(1) }
        option3TextView.setOnClickListener { selectOption(2) }
        option4TextView.setOnClickListener { selectOption(3) }

        btnSubmit = findViewById(R.id.btnSubmit)

        // Extract Quiz object from intent extra
        quiz = intent.getParcelableExtra("quiz") ?: return
        quizCode =intent.getStringExtra("quizCode") ?: return

        btnSubmit.setOnClickListener {
            checkAnswer()
            displayNextQuestion()
        }

        displayNextQuestion()
    }

    override fun onBackPressed() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Exit Quiz")
        alertDialogBuilder.setMessage("Are you sure you want to exit quiz? Your data won't be saved.")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            super.onBackPressed() // Go back if user confirms
        }
        alertDialogBuilder.setNegativeButton("No") { _, _ ->
            // Do nothing, stay in the quiz creation activity
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun displayCurrentQuestion() {
        // Cancel the previous timer if running
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }

        currentQuestionIndex++

        val questions = quiz.questions

        if (currentQuestionIndex < questions.size) {
            // Start timer for this question
            startTimer()
            option1TextView.setBackgroundResource(android.R.color.transparent)
            option2TextView.setBackgroundResource(android.R.color.transparent)
            option3TextView.setBackgroundResource(android.R.color.transparent)
            option4TextView.setBackgroundResource(android.R.color.transparent)
            selectedAnswerIndex = -1

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
            DatabaseUtils.addUserToLeaderboard(quizCode,score)
            val intent = Intent(this@QuizActivity, LeaderboardActivity::class.java)
            intent.putExtra("quizCode",quizCode)
            startActivity(intent)
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

        if (selectedAnswerIndex == correctAnswerIndex){
            incrementScore()
        }



    }

    private fun incrementScore(){
        score += milisUntilFinish / 20 + 1
    }

    private fun displayNextQuestion() {
        // Disable all option buttons
        option1TextView.isEnabled = false
        option2TextView.isEnabled = false
        option3TextView.isEnabled = false
        option4TextView.isEnabled = false
        btnSubmit.isEnabled = false

        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }

        // Start a new timer for 5 seconds
        val timer = object : CountDownTimer(5100, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update timer display
                val seconds = millisUntilFinished / 1000
                tvTimer.text = "Next question in: $seconds s"
            }

            override fun onFinish() {
                // Timer finished, enable all option buttons again
                option1TextView.isEnabled = true
                option2TextView.isEnabled = true
                option3TextView.isEnabled = true
                option4TextView.isEnabled = true
                btnSubmit.isEnabled = true
                // Display the next question after the timer ends
                displayCurrentQuestion()
            }
        }
        timer.start()
    }


    private fun startTimer() {
        countDownTimer = object : CountDownTimer(20100, 10) { // 20 seconds timer
            override fun onTick(millisUntilFinished: Long) {
                // Update timer display
                val seconds = millisUntilFinished / 1000
                tvTimer.text = "Time: $seconds s"

                milisUntilFinish = millisUntilFinished.toInt()
            }

            override fun onFinish() {
                // Timer finished, check answer and display next question
                checkAnswer()
                displayNextQuestion()
            }
        }
        countDownTimer.start()
    }

}
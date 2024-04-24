package com.example.quiztesting

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CreateQuizActivity: ComponentActivity() {

    private var questions: MutableList<Question> = mutableListOf()
    private var data: MutableList<ItemsViewModel> = mutableListOf()
    private lateinit var btAddQuestion: Button
    private lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.rwQuestions)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)


        // This loop questions
        for (question in questions) {
            data.add(ItemsViewModel(question.question))
        }

        // This will pass the ArrayList to our Adapter
        adapter = CustomAdapter(data,
            onItemClick = { position ->
                val selectedQuestion = questions[position]
                showEditQuestionPopup(selectedQuestion)
            },
            onDeleteClick = { position ->
                deleteQuestion(position)
            }
        )

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
        btAddQuestion = findViewById(R.id.btAddQuestion)

        btAddQuestion.setOnClickListener {
            showAddQuestionPopup()
        }

        val btCreateQuiz:Button = findViewById(R.id.btCreateQuiz)

        btCreateQuiz.setOnClickListener{
            createQuiz()
        }

    }


    override fun onBackPressed() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Exit Quiz Creation")
        alertDialogBuilder.setMessage("Are you sure you want to exit quiz creation? Your data won't be saved.")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            super.onBackPressed() // Go back if user confirms
        }
        alertDialogBuilder.setNegativeButton("No") { _, _ ->
            // Do nothing, stay in the quiz creation activity
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    private fun showAddQuestionPopup() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_add_question, null)
        val popupWindow = PopupWindow(
            popupView,
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.MATCH_PARENT,
            true
        )


        val etQuestion = popupView.findViewById<EditText>(R.id.etQuestion)
        val etAnswerCorrect = popupView.findViewById<EditText>(R.id.etAnswerCorrect)
        val etAnswerI1 = popupView.findViewById<EditText>(R.id.etAnswerI1)
        val etAnswerI2 = popupView.findViewById<EditText>(R.id.etAnswerI2)
        val etAnswerI3 = popupView.findViewById<EditText>(R.id.etAnswerI3)
        val btAddQuestionPopup = popupView.findViewById<Button>(R.id.btAddQuestionPopup)
        val btCancel = popupView.findViewById<Button>(R.id.btCancel)

        etQuestion.requestFocus()

        btAddQuestionPopup.setOnClickListener {
            val questionText = etQuestion.text.toString()
            val answerCorrectText = etAnswerCorrect.text.toString()
            val answerI1Text = etAnswerI1.text.toString()
            val answerI2Text = etAnswerI2.text.toString()
            val answerI3Text = etAnswerI3.text.toString()



            if (questionText.isNotEmpty() && answerCorrectText.isNotEmpty() &&
                answerI1Text.isNotEmpty() && answerI2Text.isNotEmpty() && answerI3Text.isNotEmpty()
            ) {
                if(answerCorrectText.trim().equals(answerI1Text.trim(), ignoreCase = true)){
                    etAnswerCorrect.error = "Answers can't be the same!"
                    etAnswerI1.error = "Answers can't be the same!"
                }else if(answerCorrectText.trim().equals(answerI2Text.trim(), ignoreCase = true)){
                    etAnswerCorrect.error = "Answers can't be the same!"
                    etAnswerI2.error = "Answers can't be the same!"
                }else if(answerCorrectText.trim().equals(answerI3Text.trim(), ignoreCase = true)){
                    etAnswerCorrect.error = "Answers can't be the same!"
                    etAnswerI3.error = "Answers can't be the same!"
                }else if(answerI1Text.trim().equals(answerI2Text.trim(), ignoreCase = true)){
                    etAnswerI1.error = "Answers can't be the same!"
                    etAnswerI2.error = "Answers can't be the same!"
                }else if(answerI1Text.trim().equals(answerI3Text.trim(), ignoreCase = true)){
                    etAnswerI1.error = "Answers can't be the same!"
                    etAnswerI3.error = "Answers can't be the same!"
                }else if(answerI2Text.trim().equals(answerI3Text.trim(), ignoreCase = true)){
                    etAnswerI2.error = "Answers can't be the same!"
                    etAnswerI3.error = "Answers can't be the same!"
                }else{
                    val answers = listOf(answerCorrectText, answerI1Text, answerI2Text, answerI3Text)
                    val question = Question(questionText, answers, 0) // Assuming correct answer index is 0
                    questions.add(question)
                    data.add(ItemsViewModel(question.question))

                    adapter.notifyItemInserted(data.size - 1)
                    // You can update your RecyclerView adapter here if needed
                    popupWindow.dismiss()
                }

            } else {
                if (questionText.isEmpty()) {
                    etQuestion.error = "Enter a question"
                }
                if (answerCorrectText.isEmpty()) {
                    etAnswerCorrect.error = "Enter the correct answer"
                }
                if (answerI1Text.isEmpty()) {
                    etAnswerI1.error = "Enter an incorrect answer"
                }
                if (answerI2Text.isEmpty()) {
                    etAnswerI2.error = "Enter an incorrect answer"
                }
                if (answerI3Text.isEmpty()) {
                    etAnswerI3.error = "Enter an incorrect answer"
                }
            }
        }

        btCancel.setOnClickListener {
            popupWindow.dismiss()
        }


        popupWindow.showAsDropDown(btAddQuestion)
    }


    private fun showEditQuestionPopup(question: Question) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_add_question, null)
        val popupWindow = PopupWindow(
            popupView,
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.MATCH_PARENT,
            true
        )

        val etQuestion = popupView.findViewById<EditText>(R.id.etQuestion)
        val etAnswerCorrect = popupView.findViewById<EditText>(R.id.etAnswerCorrect)
        val etAnswerI1 = popupView.findViewById<EditText>(R.id.etAnswerI1)
        val etAnswerI2 = popupView.findViewById<EditText>(R.id.etAnswerI2)
        val etAnswerI3 = popupView.findViewById<EditText>(R.id.etAnswerI3)
        val btAddQuestionPopup = popupView.findViewById<Button>(R.id.btAddQuestionPopup)
        val btCancel = popupView.findViewById<Button>(R.id.btCancel)

        etQuestion.requestFocus()
        // Fill EditText fields with question details
        etQuestion.setText(question.question)
        etAnswerCorrect.setText(question.answers[0]) // Assuming the correct answer is always at index 0
        etAnswerI1.setText(question.answers[1])
        etAnswerI2.setText(question.answers[2])
        etAnswerI3.setText(question.answers[3])

        btAddQuestionPopup.text = "Update question"

        btAddQuestionPopup.setOnClickListener {
            val questionText = etQuestion.text.toString()
            val answerCorrectText = etAnswerCorrect.text.toString()
            val answerI1Text = etAnswerI1.text.toString()
            val answerI2Text = etAnswerI2.text.toString()
            val answerI3Text = etAnswerI3.text.toString()

            if (questionText.isNotEmpty() && answerCorrectText.isNotEmpty() &&
                answerI1Text.isNotEmpty() && answerI2Text.isNotEmpty() && answerI3Text.isNotEmpty()
            ) {
                if(answerCorrectText.trim().equals(answerI1Text.trim(), ignoreCase = true)){
                    etAnswerCorrect.error = "Answers can't be the same!"
                    etAnswerI1.error = "Answers can't be the same!"
                }else if(answerCorrectText.trim().equals(answerI2Text.trim(), ignoreCase = true)){
                    etAnswerCorrect.error = "Answers can't be the same!"
                    etAnswerI2.error = "Answers can't be the same!"
                }else if(answerCorrectText.trim().equals(answerI3Text.trim(), ignoreCase = true)){
                    etAnswerCorrect.error = "Answers can't be the same!"
                    etAnswerI3.error = "Answers can't be the same!"
                }else if(answerI1Text.trim().equals(answerI2Text.trim(), ignoreCase = true)){
                    etAnswerI1.error = "Answers can't be the same!"
                    etAnswerI2.error = "Answers can't be the same!"
                }else if(answerI1Text.trim().equals(answerI3Text.trim(), ignoreCase = true)){
                    etAnswerI1.error = "Answers can't be the same!"
                    etAnswerI3.error = "Answers can't be the same!"
                }else if(answerI2Text.trim().equals(answerI3Text.trim(), ignoreCase = true)){
                    etAnswerI2.error = "Answers can't be the same!"
                    etAnswerI3.error = "Answers can't be the same!"
                }else{
                    // Update the question in the list with new values
                    val answers = listOf(answerCorrectText, answerI1Text, answerI2Text, answerI3Text)
                    val updatedQuestion = Question(questionText, answers, 0) // Assuming correct answer index is 0
                    val index = questions.indexOf(question)
                    if (index != -1) {
                        questions[index] = updatedQuestion
                        data[index] = ItemsViewModel(questionText) // Update data list
                        adapter.notifyItemChanged(index)
                    }
                    popupWindow.dismiss()
                }
            } else {
                if (questionText.isEmpty()) {
                    etQuestion.error = "Enter a question"
                }
                if (answerCorrectText.isEmpty()) {
                    etAnswerCorrect.error = "Enter the correct answer"
                }
                if (answerI1Text.isEmpty()) {
                    etAnswerI1.error = "Enter an incorrect answer"
                }
                if (answerI2Text.isEmpty()) {
                    etAnswerI2.error = "Enter an incorrect answer"
                }
                if (answerI3Text.isEmpty()) {
                    etAnswerI3.error = "Enter an incorrect answer"
                }
            }
        }

        btCancel.setOnClickListener {
            popupWindow.dismiss()
        }
        // Clear focus from etName and etmDescription
        findViewById<EditText>(R.id.etName).clearFocus()
        findViewById<EditText>(R.id.etmDescription).clearFocus()

        popupWindow.showAsDropDown(btAddQuestion)
    }

    private fun deleteQuestion(position:Int){
        val questionToDelete = questions[position]
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Delete Question")
        alertDialogBuilder.setMessage("Are you sure you want to delete this question: \"${questionToDelete.question}\"?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // Handle delete action here
            questions.removeAt(position)
            data.removeAt(position)
            adapter.notifyItemRemoved(position)
            // You may also need to update any other related data or views
        }
        alertDialogBuilder.setNegativeButton("No") { _, _ ->
            // Do nothing, just close the dialog
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun createQuiz(){
        val quizName = findViewById<EditText>(R.id.etName).text.toString().trim()
        val quizDescription = findViewById<EditText>(R.id.etmDescription).text.toString().trim()
        val errorMessage = when {
            quizName.isEmpty() -> "Please enter a quiz name."
            questions.isEmpty() -> "Please add at least one question to the quiz."
            else -> null // No error message if everything is valid
        }

        if (errorMessage != null) {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Error")
            alertDialogBuilder.setMessage(errorMessage)
            alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                // Dismiss the dialog
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        } else {
            val newQuiz = Quiz(quizName,quizDescription, questions)
            DatabaseUtils.addNewQuiz(newQuiz, object : DatabaseUtils.AddQuizListener {
                override fun onQuizAdded(key: String) {
                    Log.d("CreateQuiz", "New quiz added with key: $key")
                    val intent = Intent(this@CreateQuizActivity, QuizCreatedActivity::class.java)
                    intent.putExtra("quiz_code", key)
                    startActivity(intent)
                }

                override fun onAddQuizError(error: String) {
                    Log.e("CreateQuiz", "Error adding quiz: $error")
                }
            })
        }

    }

}
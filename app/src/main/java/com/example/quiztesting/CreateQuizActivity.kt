package com.example.quiztesting

import android.app.AlertDialog
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

        btAddQuestionPopup.setOnClickListener {
            val questionText = etQuestion.text.toString()
            val answerCorrectText = etAnswerCorrect.text.toString()
            val answerI1Text = etAnswerI1.text.toString()
            val answerI2Text = etAnswerI2.text.toString()
            val answerI3Text = etAnswerI3.text.toString()

            if (questionText.isNotEmpty() && answerCorrectText.isNotEmpty() &&
                answerI1Text.isNotEmpty() && answerI2Text.isNotEmpty() && answerI3Text.isNotEmpty()
            ) {
                val answers = listOf(answerCorrectText, answerI1Text, answerI2Text, answerI3Text)
                val question = Question(questionText, answers, 0) // Assuming correct answer index is 0
                questions.add(question)
                data.add(ItemsViewModel(question.question))

                adapter.notifyItemInserted(data.size - 1)
                // You can update your RecyclerView adapter here if needed
                popupWindow.dismiss()
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

    private fun deleteQuestion(position:Int){
        val questionToDelete = questions[position]
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Delete Question")
        alertDialogBuilder.setMessage("Are you sure you want to delete this question: \"${questionToDelete.question}\"?")
        alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
            // Handle delete action here
            questions.removeAt(position)
            data.removeAt(position)
            adapter.notifyItemRemoved(position)
            // You may also need to update any other related data or views
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            // Do nothing, just close the dialog
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun createQuiz(){

    }

}
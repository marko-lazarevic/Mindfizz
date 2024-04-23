package com.example.quiztesting

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CreateQuizActivity: ComponentActivity() {

    private var quizName:String = ""
    private var quizDescription:String = ""
    private var questions: MutableList<Question> = mutableListOf()
    private lateinit var btAddQuestion: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.rwQuestions)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..20) {
            data.add(ItemsViewModel(R.drawable.itemimage, "Item" + i))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
        btAddQuestion = findViewById<Button>(R.id.btAddQuestion)

        btAddQuestion.setOnClickListener {
            showAddQuestionPopup()
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
                // You can update your RecyclerView adapter here if needed
                popupWindow.dismiss()
            } else {
                // Handle case where fields are empty
            }
        }

        btCancel.setOnClickListener {
            popupWindow.dismiss()
        }


        popupWindow.showAsDropDown(btAddQuestion)
    }


}
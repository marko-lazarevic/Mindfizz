package com.example.quiztesting

data class Quiz(
    val name: String = "",
    val description:String="",
    val questions: List<Question> = listOf()
)

data class Question(
    val question:String = "",
    val answers:List<String> = listOf(),
    val correctAnswer:Int = 0
)

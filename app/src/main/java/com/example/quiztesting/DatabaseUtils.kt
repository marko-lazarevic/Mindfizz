package com.example.quiztesting
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object DatabaseUtils {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val quizzesRef: DatabaseReference = database.getReference("quizzes")

    interface QuizLoadListener {
        fun onQuizLoaded(quiz: Quiz)
        fun onQuizNotFound(quizCode: String)
        fun onDatabaseError(error: String)
    }

    fun loadQuizByKey(quizCode: String, listener: QuizLoadListener) {
        quizzesRef.child(quizCode).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val quiz = dataSnapshot.getValue(Quiz::class.java)
                    if (quiz != null) {
                        listener.onQuizLoaded(quiz)
                    } else {
                        listener.onDatabaseError("Failed to parse quiz data")
                    }
                } else {
                    listener.onQuizNotFound(quizCode)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listener.onDatabaseError(databaseError.message)
            }
        })
    }
}

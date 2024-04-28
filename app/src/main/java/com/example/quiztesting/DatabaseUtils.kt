package com.example.quiztesting
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object DatabaseUtils {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val quizzesRef: DatabaseReference = database.getReference("quizzes")
    private val leadboardRef: DatabaseReference = database.getReference("leadboard")

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    interface QuizLoadListener {
        fun onQuizLoaded(quiz: Quiz)
        fun onQuizNotFound(quizCode: String)
        fun onDatabaseError(error: String)
    }

    interface BoardLoadListener {
        fun onBoardLoaded(board: MutableList<LeaderboardEntry>)
        fun onBoardNotFound(quizCode: String)
        fun onDatabaseError(error: String)
    }

    interface AddQuizListener {
        fun onQuizAdded(key: String)
        fun onAddQuizError(error: String)
    }

    interface CreateUserListener {
        fun onUserCreated(user: FirebaseUser)
        fun onUserCreationFailed(error: String)
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

    fun loadLeadboardByKey(quizCode: String, listener: BoardLoadListener){
        leadboardRef.child(quizCode).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    val entries = mutableListOf<LeaderboardEntry>()
                    dataSnapshot.children.forEach { child ->
                        val entry = child.getValue(LeaderboardEntry::class.java)
                        entry?.let {
                            entries.add(it)
                        }
                    }
                    listener.onBoardLoaded(entries)

                } else {
                    listener.onBoardNotFound(quizCode)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listener.onDatabaseError(databaseError.message)
            }
        })
    }

    fun addNewQuiz(quiz: Quiz, listener: AddQuizListener):String {
        val key = quizzesRef.push().key // Generate a new unique key
        if (key != null) {
            quizzesRef.child(key).setValue(quiz)
                .addOnSuccessListener { listener.onQuizAdded(key) }
                .addOnFailureListener { listener.onAddQuizError("Failed to add quiz") }

        } else {
            listener.onAddQuizError("Failed to generate key")
        }
        return key?:""
    }

    fun createUser(displayName: String, listener: CreateUserListener) {
        // Check if user is already signed in anonymously
        if (auth.currentUser != null) {
            listener.onUserCreated(auth.currentUser!!)
            return
        }

        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    // Update display name for the anonymous user
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()

                    user.updateProfile(profileUpdates).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            listener.onUserCreated(user)
                        } else {
                            listener.onUserCreationFailed("Failed to update display name")
                        }
                    }
                } else {
                    listener.onUserCreationFailed("User is null after anonymous sign-in")
                }
            } else {
                listener.onUserCreationFailed("Anonymous authentication failed")
            }
        }
    }

    fun addUserToLeaderboard(quizCode: String, score: Int) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val uid = user.uid
            val displayName = user.displayName

            if (uid != null && displayName != null) {
                val leaderboardEntry = LeaderboardEntry(displayName, uid, score)
                leadboardRef.child(quizCode).push().setValue(leaderboardEntry)
                    .addOnSuccessListener {
                        // Successfully added user to leaderboard
                        Log.d("DatabseUtils", "User added to leaderboard for quiz code: $quizCode")
                    }
                    .addOnFailureListener { e ->
                        // Handle failure
                        Log.e("DatabseUtils", "Failed to add user to leaderboard: $e")
                    }
            } else {
                Log.e("DatabseUtils", "User ID or display name is null.")
            }
        } ?: Log.e("DatabseUtils", "Current user is null.")
    }
}

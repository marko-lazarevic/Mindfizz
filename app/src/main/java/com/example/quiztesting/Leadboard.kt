package com.example.quiztesting

class LeaderboardEntry(val name:String="", val uid: String = "", val score: Int = 0){
    // No-argument constructor
    constructor() : this("", "", 0)
}

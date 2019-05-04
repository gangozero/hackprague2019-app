package com.gangozero.prague.app.profiles

data class Grade(
        val profile_id:String,
        val user_id: String,
        val grade: Int,
        val lat: Double,
        val lon: Double
)
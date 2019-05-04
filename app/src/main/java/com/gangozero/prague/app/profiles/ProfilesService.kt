package com.gangozero.prague.app.profiles

class ProfilesService {
    fun getProfiles(): List<Profile> {
        Thread.sleep(1000)
        return listOf(
                Profile("wheel-chair", "1"),
                Profile("blind", "2")
        )
    }
}
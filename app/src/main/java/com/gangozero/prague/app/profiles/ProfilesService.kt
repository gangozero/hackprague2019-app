package com.gangozero.prague.app.profiles

class ProfilesService {
    fun getProfiles(): List<Profile> {
        return listOf(
                Profile("wheel-chair", "1")
        )
    }
}
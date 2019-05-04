package com.gangozero.prague.app.profiles.usecases

import com.gangozero.prague.app.core.UseCase
import com.gangozero.prague.app.profiles.Profile
import com.gangozero.prague.app.profiles.ProfilesService

class GetProfiles(
        private val profilesService: ProfilesService
) : UseCase<Unit, List<Profile>> {
    override fun get(unit: Unit): List<Profile> {
        return profilesService.getProfiles()
    }
}
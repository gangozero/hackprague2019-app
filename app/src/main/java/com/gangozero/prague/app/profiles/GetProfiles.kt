package com.gangozero.prague.app.profiles

import com.gangozero.prague.app.core.UseCase

class GetProfiles(
        private val profilesService: ProfilesService
) : UseCase<List<Profile>> {
    override fun get(): List<Profile> {
        return profilesService.getProfiles()
    }
}
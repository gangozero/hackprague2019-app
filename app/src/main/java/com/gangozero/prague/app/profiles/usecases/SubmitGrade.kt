package com.gangozero.prague.app.profiles.usecases

import com.gangozero.prague.app.core.UseCase
import com.gangozero.prague.app.profiles.Grade
import com.gangozero.prague.app.profiles.ProfilesService

class SubmitGrade(val profilesService: ProfilesService) : UseCase<Grade, Unit> {
    override fun get(input: Grade) {
        profilesService.submitOpinion(input)
    }
}
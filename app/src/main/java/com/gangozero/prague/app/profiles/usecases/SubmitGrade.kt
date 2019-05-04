package com.gangozero.prague.app.profiles.usecases

import android.util.Log
import com.gangozero.prague.app.core.UseCase
import com.gangozero.prague.app.core.location.LocationManager
import com.gangozero.prague.app.profiles.Profile
import com.gangozero.prague.app.profiles.ProfilesService

class SubmitGrade(
        val profilesService: ProfilesService,
        val locationManager: LocationManager,
        val currentProfileGetter: () -> Profile?
) : UseCase<Boolean, Unit> {
    override fun get(input: Boolean) {

        val lastLocation = locationManager.lastLocation
        val profile = currentProfileGetter()
        if (lastLocation != null && profile != null) {
            profilesService.submitOpinion(
                    profile.id,
                    "1",
                    input,
                    lastLocation.latitude,
                    lastLocation.longitude

            )
        } else {
            Log.d("grade", "no: $lastLocation or $profile")
        }
    }
}
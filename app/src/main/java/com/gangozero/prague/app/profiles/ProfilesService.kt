package com.gangozero.prague.app.profiles

import android.util.Log
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class ProfilesService {

    var service: Api

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://backend-j5xvupqgfq-uc.a.run.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create(Api::class.java)
    }

    fun submitOpinion(profileId: String,
                      userId: String,
                      like: Boolean,
                      lat: Double,
                      lon: Double) {

        val response = service.postGrade(profileId, Grade(userId, if (like) 1 else -1, lat, lon)).execute()
        Log.d("grade", "resp: $response")
    }

    fun getProfiles(): List<Profile> {

        val profiles = service.getProfiles()
        val execute = profiles.execute()

        if (execute.isSuccessful && execute.code() == 200) {
            return execute.body()!!.sortedBy { it.name }
        } else {
            throw RuntimeException("Network Error: ${execute.code()}, ${execute.message()}")
        }
    }

    interface Api {
        @GET("/api/v1/profile")
        fun getProfiles(): Call<List<Profile>>

        @POST("/api/v1/grade/{profileId}")
        fun postGrade(@Path("profileId") profileId: String, @Body gradle: Grade): Call<Unit>
    }

    class Response
}
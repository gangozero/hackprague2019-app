package com.gangozero.prague.app.profiles

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
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create(Api::class.java)
    }

    fun submitOpinion(grade:Grade) {
        //service.postGrade(profileId, Grade(userId, if (like) 1 else -1, lat, lon)).execute()
    }

    fun getProfiles(): List<Profile> {

//        service.getProfiles().execute().body()

        Thread.sleep(1000)
        return listOf(
                Profile("wheel-chair", "1"),
                Profile("blind", "2")
        )
    }

    interface Api {
        @GET("/profile")
        fun getProfiles(): Call<List<Profile>>

        @POST("/grade/{profileId}")
        fun postGrade(@Path("profileId") profileId: String, @Body gradle: Grade): Call<Response>
    }

    data class Response(val message: String)
}
package com.example.palapp.network

import com.example.palapp.data.LoginResponse
import com.example.palapp.data.ProfileCreatedResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


private const val BASE_URL = "https://d040-103-85-119-28.ngrok-free.app"

val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface LogInApiService{


    @GET("/api/haveprofile/")
    suspend fun getData(@Header("Authorization") accessToken: String): Call<ProfileCreatedResponse>
}
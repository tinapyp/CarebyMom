package com.dicoding.carebymom.data.api

import com.dicoding.carebymom.data.response.LoginResponse
import com.dicoding.carebymom.data.response.PredictResponse
import com.dicoding.carebymom.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.math.BigInteger

interface ApiService {
    @FormUrlEncoded
    @POST("signup")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("periodTime") periodTime: BigInteger
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    data class PredictRequest(
        val Age: Double,
        val Pregnancy_Duration: Double,
        val Weight_kg: Double,
        val Height_cm: Double,
        val BMI_Score: Double,
        val Arm_Circumference: Double,
        val Fundus_Height: Double,
        val Heart_Rate: Double
    )

    @POST("predict")
    suspend fun predict(@Body request: PredictRequest): PredictResponse

}
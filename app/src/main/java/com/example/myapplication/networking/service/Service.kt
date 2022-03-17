package com.example.myapplication.networking.service

import com.example.networkingexam.model.Card
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

@JvmSuppressWildcards
interface Service {

    @GET("payments")
    fun getCards(): Call<List<Card>>

    @POST("payments")
    fun addCard(@Body card: Card): Call<Card>

}
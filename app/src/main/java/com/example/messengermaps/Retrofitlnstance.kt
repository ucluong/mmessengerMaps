package com.example.messengermaps


import com.example.messengermaps.`interface`.NotificationApi
import com.google.firebase.messaging.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class Retrofitlnstance {
    companion object{
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.FCM_WAKE_LOCK)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val api by lazy {
            retrofit.create(NotificationApi::class.java)
        }
    }
}
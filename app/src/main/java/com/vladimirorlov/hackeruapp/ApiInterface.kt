package com.vladimirorlov.hackeruapp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("?key=29958309-9a80dea824f08f90c70b785ca&image_type=photo&pretty=true&")
    fun getImages(@Query("q") noteTitle: String): Call<ApiResponse>

    companion object {
        val BASE_URL = "https://pixabay.com/api/"
        fun create(): ApiInterface {
            val retrofit = Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}
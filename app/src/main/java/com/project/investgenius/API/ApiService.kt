package com.project.investgenius.API

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("top-gainers") // Use the correct endpoint
    fun getTopGainers(): Call<Map<String, List<Gainer>>>
}
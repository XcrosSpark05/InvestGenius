package com.project.investgenius.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.investgenius.API.ApiService
import com.project.investgenius.API.Gainer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class HomeViewModel : ViewModel() {

    private val _topGainers = MutableLiveData<List<Gainer>>()
    val topGainers: LiveData<List<Gainer>> = _topGainers

    fun fetchTopGainers() {
        // If data is already loaded, no need to fetch again
        if (_topGainers.value != null && _topGainers.value!!.isNotEmpty()) return

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://apitesting-production-07ba.up.railway.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getTopGainers().enqueue(object : Callback<Map<String, List<Gainer>>> {
            override fun onResponse(
                call: Call<Map<String, List<Gainer>>>,
                response: Response<Map<String, List<Gainer>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { responseData ->
                        val gainersList = responseData["top_gainers"] ?: emptyList()
                        _topGainers.value = gainersList
                    }
                }
            }

            override fun onFailure(call: Call<Map<String, List<Gainer>>>, t: Throwable) {
                // Optionally handle error (e.g., log or update another LiveData)
            }
        })
    }
}

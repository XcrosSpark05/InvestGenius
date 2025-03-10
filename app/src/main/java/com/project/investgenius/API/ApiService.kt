package com.project.investgenius.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-gainers") // Use the correct endpoint
    fun getTopGainers(): Call<Map<String, List<Gainer>>>

    @GET("all-stocks")
    fun getAllStocks(): Call<Map<String, List<Gainer>>>

    @GET("indian-indices")
    fun getIndianIndices(): Call<IndianIndices>

    @GET("chat")
    fun sendMessage(@Query("message") message: String): Call<ChatResponse>

    @GET("portfolio_advice")
    fun getPortfolioAdvice(
        @Query("symbol") symbol: String,
        @Query("quantity") quantity: Int,
        @Query("goal") goal: String
    ): Call<PortfolioAdviceResponse>

    @GET("stock")
    fun getStockInfo(
        @Query("symbol") symbol: String,
        @Query("goal") goal: String
    ): Call<StockInfoResponse>

    @GET("wordinfo")
    fun getWordInfo(@Query("word") word: String): Call<WordInfoResponse>

    @GET("news")
    fun getNews(@Query("topic") topic: String): Call<NewsResponse>
}


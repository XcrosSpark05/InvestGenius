package com.project.investgenius.API

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gainer(
    val symbol: String,
    val company_name: String,
    val price: Double,
    val price_change: Double,
    val imageUrl: String,
    val timestamp: String
) : Parcelable

@Parcelize
data class IndexInfo(
    @SerializedName("Current Value")
    val currentValue: Double
) : Parcelable

@Parcelize
data class IndianIndices(
    @SerializedName("indian_indices")
    val indices: Map<String, IndexInfo>
) : Parcelable

data class ChatResponse(
    val response: String
)

data class PortfolioAdviceResponse(
    val symbol: String,
    val quantity: Int,
    @SerializedName("stock_details")
    val stockDetails: StockDetails,
    @SerializedName("news_sentiment")
    val newsSentiment: NewsSentiment,
    @SerializedName("predicted_price")
    val predictedPrice: Double,
    val advice: String
)

data class StockInfoResponse(
    @SerializedName("stock_details")
    val stockDetails: StockDetails,
    @SerializedName("news_sentiment")
    val newsSentiment: NewsSentiment,
    @SerializedName("predicted_price")
    val predictedPrice: Double,
    val advice: String
)

data class StockDetails(
    @SerializedName("current_price")
    val currentPrice: Double,
    @SerializedName("market_cap")
    val marketCap: Any?,
    @SerializedName("pe_ratio")
    val peRatio: Any?,
    @SerializedName("52_week_high")
    val weekHigh52: Any?,
    @SerializedName("52_week_low")
    val weekLow52: Any?,
    @SerializedName("sma_50")
    val sma50: Any?,
    @SerializedName("sma_200")
    val sma200: Any?
)

data class NewsSentiment(
    val sentiment: String,
    @SerializedName("average_score")
    val averageScore: Double,
    @SerializedName("news_articles")
    val newsArticles: List<Any>
)

data class WordInfoResponse(
    val word: String,
    val meaning: String,
    @SerializedName("wiki_summary")
    val wikiSummary: String
)

data class NewsResponse(
    val articles: List<NewsArticle>
)

data class NewsArticle(
    val title: String,
    val description: String,
    val url: String
)
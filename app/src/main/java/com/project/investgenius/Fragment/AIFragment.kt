package com.project.investgenius.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.project.investgenius.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.project.investgenius.API.RetrofitClient
import com.project.investgenius.API.ChatResponse
import com.project.investgenius.API.StockInfoResponse
import com.project.investgenius.API.WordInfoResponse
import com.project.investgenius.API.NewsResponse


class AIFragment : Fragment() {

    private lateinit var chatDisplay: TextView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a_i, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements
        chatDisplay = view.findViewById(R.id.chatDisplay)
        messageInput = view.findViewById(R.id.messageInput)
        sendButton = view.findViewById(R.id.sendButton)

        // Set up click listener for the send button
        sendButton.setOnClickListener {
            val userMessage = messageInput.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                chatDisplay.append("\nYou: $userMessage\n")

                // Route the input based on its content
                handleUserMessage(userMessage)

                // Append a separator after handling the message
                chatDisplay.append("_________________________\n")

                // Clear input field
                messageInput.text.clear()
            }
        }
    }

    private fun handleUserMessage(message: String) {
        val lowerMessage = message.lowercase()
        when {
            message.split(" ").size == 1 -> {
                getStockInfo(message, "investor")
            }
            lowerMessage.startsWith("what is the meaning of") -> {
                val tokens = message.split(" ", limit = 5)
                if (tokens.size >= 5) {
                    val word = tokens[4]
                    getWordInfo(word)
                } else {
                    chatDisplay.append("\nBot: Please use the format: what is the meaning of <word>")
                }
            }
            lowerMessage.startsWith("tell me the news of") -> {
                val topic = message.substringAfter("tell me the news of").trim()
                if (topic.isNotEmpty()) {
                    getNews(topic)
                } else {
                    chatDisplay.append("\nBot: Please use the format: tell me the news of <topic>")
                }
            }
            else -> {
                sendChatMessage(message)
            }
        }
    }

    private fun sendChatMessage(message: String) {
        RetrofitClient.instance.sendMessage(message)
            .enqueue(object : Callback<ChatResponse> {
                override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                    if (response.isSuccessful) {
                        val botResponse = response.body()?.response ?: "No response"
                        chatDisplay.append("\nBot: $botResponse")
                    } else {
                        chatDisplay.append("\nBot: Error: ${response.errorBody()?.string()}")
                    }
                }
                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    chatDisplay.append("\nBot: Request failed: ${t.message}")
                }
            })
    }

    private fun getStockInfo(symbol: String, goal: String) {
        RetrofitClient.instance.getStockInfo(symbol, goal)
            .enqueue(object : Callback<StockInfoResponse> {
                override fun onResponse(call: Call<StockInfoResponse>, response: Response<StockInfoResponse>) {
                    if (response.isSuccessful) {
                        val stockInfo = response.body()
                        val currentPrice = stockInfo?.stockDetails?.currentPrice ?: "N/A"
                        val predictedPrice = stockInfo?.predictedPrice ?: "N/A"
                        val advice = stockInfo?.advice ?: "No advice available"

                        val infoMessage = """
                            Bot: 
                            Current Price: $currentPrice
                            Predicted Price: $predictedPrice
                            Advice: $advice
                            _________________________
                        """.trimIndent()

                        chatDisplay.append("\n$infoMessage")
                    } else {
                        chatDisplay.append("\nBot: Error: ${response.errorBody()?.string()}")
                    }
                }
                override fun onFailure(call: Call<StockInfoResponse>, t: Throwable) {
                    chatDisplay.append("\nBot: Request failed: ${t.message}")
                }
            })
    }

    private fun getWordInfo(word: String) {
        RetrofitClient.instance.getWordInfo(word)
            .enqueue(object : Callback<WordInfoResponse> {
                override fun onResponse(call: Call<WordInfoResponse>, response: Response<WordInfoResponse>) {
                    if (response.isSuccessful) {
                        val wordInfo = response.body()
                        val meaning = wordInfo?.meaning ?: "No meaning found"
                        val wikiSummary = wordInfo?.wikiSummary ?: "No Wikipedia info available"
                        chatDisplay.append("\nBot: Meaning of \"$word\": $meaning\nWikipedia Info: $wikiSummary")
                    } else {
                        chatDisplay.append("\nBot: Error: ${response.errorBody()?.string()}")
                    }
                }
                override fun onFailure(call: Call<WordInfoResponse>, t: Throwable) {
                    chatDisplay.append("\nBot: Request failed: ${t.message}")
                }
            })
    }

    private fun getNews(topic: String) {
        RetrofitClient.instance.getNews(topic)
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    if (response.isSuccessful) {
                        val newsResponse = response.body()
                        val articles = newsResponse?.articles ?: listOf()
                        val newsText = articles.joinToString(separator = "\n") { article ->
                            article.title
                        }
                        chatDisplay.append("\nBot: News for \"$topic\":\n$newsText")
                    } else {
                        chatDisplay.append("\nBot: Error: ${response.errorBody()?.string()}")
                    }
                }
                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    chatDisplay.append("\nBot: Request failed: ${t.message}")
                }
            })
    }
}

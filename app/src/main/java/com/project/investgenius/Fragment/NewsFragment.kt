package com.project.investgenius.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import com.project.investgenius.R
import com.project.investgenius.adaptor.NewsRecyclerAdaptor

class NewsFragment : Fragment() {

    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var newsAdapter: NewsRecyclerAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Optionally, you can also call getNews() in onViewCreated
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment; ensure your layout has the correct IDs.
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressbar)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsRecyclerAdaptor(mutableListOf())
        recyclerView.adapter = newsAdapter

        // Start fetching news
        getNews()
    }

    private fun getNews() {
        // Replace with your actual News API key
        val newsApiClient = NewsApiClient("b7af606cdfa0434e9a8293e12911546e")
        val topHeadlinesRequest = TopHeadlinesRequest.Builder()
            .language("en")  // Optionally set language
            .build()

        progressBar.visibility = View.VISIBLE

        newsApiClient.getTopHeadlines(topHeadlinesRequest, object : NewsApiClient.ArticlesResponseCallback {
            override fun onSuccess(response: ArticleResponse) {
                activity?.runOnUiThread {
                    progressBar.visibility = View.GONE
                    // Log each article title (for debugging)
                    response.getArticles().forEach { article ->
                        Log.i("NewsFragment", article.getTitle())
                    }
                    newsAdapter.updateData(response.getArticles())
                    newsAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(throwable: Throwable) {
                activity?.runOnUiThread {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error: ${throwable.message}", Toast.LENGTH_SHORT).show()
                }
                Log.e("NewsFragment", "GOT Failure: ${throwable.message}")
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = NewsFragment()
    }
}

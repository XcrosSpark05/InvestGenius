package com.project.investgenius.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.investgenius.API.ApiService
import com.project.investgenius.API.Gainer
import com.project.investgenius.R
import com.project.investgenius.adaptor.TopGainerAdaptor
import com.project.investgenius.databinding.FragmentWatchlistBinding
import com.project.investgenius.SearchStocksViewModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WatchlistFragment : Fragment() {

    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TopGainerAdaptor

    // Store the complete list fetched from the API
    private var fullStocksList: List<Gainer> = emptyList()

    // Obtain the ViewModel
    private val viewModel: SearchStocksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter with an empty list
        adapter = TopGainerAdaptor(emptyList())
        binding.BottomSheetTopGainer.layoutManager = LinearLayoutManager(requireContext())
        binding.BottomSheetTopGainer.adapter = adapter

        // Set up the SearchView listener
        setupSearchView()

        // Observe the cached data in the ViewModel
        viewModel.fullStocksList.observe(viewLifecycleOwner) { stocks ->
            if (stocks != null) {
                fullStocksList = stocks
                adapter.updateData(fullStocksList)
                binding.progressBar.visibility = View.GONE
            }
        }

        // Fetch all stocks only if data is not already loaded
        if (viewModel.fullStocksList.value == null) {
            fetchAllStocks()
        }
    }

    private fun fetchAllStocks() {
        binding.progressBar.visibility = View.VISIBLE

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
        apiService.getAllStocks().enqueue(object : Callback<Map<String, List<Gainer>>> {
            override fun onResponse(
                call: Call<Map<String, List<Gainer>>>,
                response: Response<Map<String, List<Gainer>>>
            ) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    response.body()?.let { responseData ->
                        fullStocksList = responseData["all_stocks"] ?: emptyList()
                        // Cache the full list in the ViewModel
                        viewModel.setStocks(fullStocksList)
                        adapter.updateData(fullStocksList)
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load stocks", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, List<Gainer>>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterStocks(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterStocks(newText)
                return true
            }
        })
    }

    private fun filterStocks(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            fullStocksList
        } else {
            fullStocksList.filter { stock ->
                stock.symbol.contains(query, ignoreCase = true) ||
                        stock.company_name.contains(query, ignoreCase = true)
            }
        }
        adapter.updateData(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

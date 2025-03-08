package com.project.investgenius.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.project.investgenius.R
import com.project.investgenius.TopGainerBottomSheetFragment
import com.project.investgenius.adaptor.IndicesAdaptor
import com.project.investgenius.adaptor.TopGainerAdaptor
import com.project.investgenius.databinding.FragmentHomeBinding
import com.project.investgenius.API.ApiService
import com.project.investgenius.API.Gainer
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var gainerAdaptor: TopGainerAdaptor
    private var retryCount = 0
    private val maxRetries = 3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.textView31.setOnClickListener {
            // Assume gainerAdaptor holds the fetched list of gainers
            val currentGainers = ArrayList(gainerAdaptor.getData()) // Implement getData() in your adapter
            val bottomSheetDialog = TopGainerBottomSheetFragment.newInstance(currentGainers)
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = arrayListOf(
            SlideModel(R.drawable.banner1, ScaleTypes.FIT),
            SlideModel(R.drawable.banner2, ScaleTypes.FIT),
            SlideModel(R.drawable.banner3, ScaleTypes.FIT),
            SlideModel(R.drawable.banner4, ScaleTypes.FIT)
        )
        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)

        // Initialize Adapter with empty list
        gainerAdaptor = TopGainerAdaptor(emptyList())
        binding.topGainersRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.topGainersRecyclerView.adapter = gainerAdaptor

        fetchTopGainers()
        setupIndices()
    }

    private fun fetchTopGainers() {
        // Show the progress bar before making the network call
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
        apiService.getTopGainers().enqueue(object : Callback<Map<String, List<Gainer>>> {
            override fun onResponse(
                call: Call<Map<String, List<Gainer>>>,
                response: Response<Map<String, List<Gainer>>>
            ) {
                // Hide progress bar once data is loaded
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    response.body()?.let { responseData ->
                        val gainersList = responseData["top_gainers"] ?: emptyList()
                        gainerAdaptor.updateData(gainersList)
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, List<Gainer>>>, t: Throwable) {
                // Hide progress bar on error
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupIndices() {
        val indicesName = listOf("NIFTY 50", "SENSEX", "BANK NIFTY")
        val indicesValue = listOf(22795.90f, 75311.06f, 48981.20f)
        val indicesAdaptor = IndicesAdaptor(indicesName, indicesValue)
        binding.indicesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.indicesRecyclerView.adapter = indicesAdaptor
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
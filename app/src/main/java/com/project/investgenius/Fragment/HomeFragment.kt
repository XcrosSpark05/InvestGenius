package com.project.investgenius.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.project.investgenius.R
import com.project.investgenius.TopGainerBottomSheetFragment
import com.project.investgenius.adaptor.IndicesAdaptor
import com.project.investgenius.adaptor.TopGainerAdaptor
import com.project.investgenius.databinding.FragmentHomeBinding
import com.project.investgenius.viewmodel.HomeViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var gainerAdaptor: TopGainerAdaptor
    // Variable to hold the full list of gainers from the API
    private var fullGainers: List<com.project.investgenius.API.Gainer> = emptyList()

    // Use the ViewModel by delegation
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.textView31.setOnClickListener {
            // Pass full list of gainers to BottomSheetFragment
            val currentGainers = ArrayList(fullGainers)
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

        // Observe the LiveData in the ViewModel
        homeViewModel.topGainers.observe(viewLifecycleOwner) { gainers ->
            if (gainers != null) {
                // Save the full list for the bottom sheet
                fullGainers = gainers
                // Display only the top 4 on the home screen
                val top4 = gainers.take(4)
                gainerAdaptor.updateData(top4)
                binding.progressBar.visibility = View.GONE
            }
        }

        // Trigger fetching only if data hasn't been loaded yet
        homeViewModel.fetchTopGainers()
        setupIndices()
    }

    private fun setupIndices() {
        // Create Retrofit instance for fetching indices
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apitesting-production-07ba.up.railway.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(com.project.investgenius.API.ApiService::class.java)
        apiService.getIndianIndices().enqueue(object : retrofit2.Callback<com.project.investgenius.API.IndianIndices> {
            override fun onResponse(
                call: retrofit2.Call<com.project.investgenius.API.IndianIndices>,
                response: retrofit2.Response<com.project.investgenius.API.IndianIndices>
            ) {
                if (response.isSuccessful) {
                    val indicesResponse = response.body()
                    val indicesNameList = mutableListOf<String>()
                    val indicesValueList = mutableListOf<Float>()

                    indicesResponse?.indices?.forEach { (name, info) ->
                        indicesNameList.add(name)
                        indicesValueList.add(info.currentValue.toFloat())
                    }
                    val indicesAdaptor = IndicesAdaptor(indicesNameList, indicesValueList)
                    binding.indicesRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.indicesRecyclerView.adapter = indicesAdaptor
                } else {
                    Toast.makeText(requireContext(), "Failed to load indices", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<com.project.investgenius.API.IndianIndices>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.project.investgenius.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.project.investgenius.R
import com.project.investgenius.TopGainerBottomSheetFragment
import com.project.investgenius.adaptor.IndicesAdaptor
import com.project.investgenius.adaptor.TopGainerAdaptor
import com.project.investgenius.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.textView31.setOnClickListener{
            val bottomSheetDailog = TopGainerBottomSheetFragment()
            bottomSheetDailog.show(parentFragmentManager,"Test")
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner4, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        // Top Gainers
        val ComName = listOf("JSW Energy", "Hindalco", "Varun Beverages", "Tata Steel")
        val ComSym = listOf("JSWENERGY", "HINDALCO", "VBL", "TATASTEEL")
        val Value = listOf("₹496.60", "₹653.55", "₹477.65", "₹140.76")
        val topImages = listOf(R.drawable.stock1, R.drawable.stock2, R.drawable.stock3, R.drawable.stock4)

        val gainerAdaptor = TopGainerAdaptor(ComSym, ComName, topImages, Value) // ✅ Fixed order
        binding.topGainersRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.topGainersRecyclerView.adapter = gainerAdaptor // ✅ Fixed RecyclerView binding

        // Indices
        val indicesName = listOf("NIFTY 50", "SENSEX", "BANK NIFTY")
        val indicesValue = listOf(22795.90f, 75311.06f, 48981.20f)
        val indicesAdaptor = IndicesAdaptor(indicesName, indicesValue)
        binding.indicesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.indicesRecyclerView.adapter = indicesAdaptor
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


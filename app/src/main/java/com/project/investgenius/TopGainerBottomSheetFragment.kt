package com.project.investgenius

import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.investgenius.adaptor.TopGainerAdaptor
import com.project.investgenius.databinding.FragmentTopGainerBottomSheetBinding

class TopGainerBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentTopGainerBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTopGainerBottomSheetBinding.inflate(inflater,container,false)

        val ComName3 = listOf("JSW Energy", "Hindalco", "Varun Beverages", "Tata Steel", "Macrotech Devis", "SBI Life Insurance", "Hindalco", "Varun Beverages", "Tata Steel", "Macrotech Devis", "SBI Life Insurance")
        val ComSym3 = listOf("JSWENERGY", "HINDALCO", "VBL", "TATASTEEL", "LODHA", "SBILIFE", "HINDALCO", "VBL", "TATASTEEL", "LODHA", "SBILIFE")
        val Value3 = listOf("₹496.60", "₹653.55", "₹477.65", "₹140.76", "₹1,215.55", "₹1,495.40", "₹653.55", "₹477.65", "₹140.76", "₹1,215.55", "₹1,495.40")
        val topImages3 = listOf(R.drawable.stock1, R.drawable.stock2, R.drawable.stock3, R.drawable.stock4, R.drawable.stock5, R.drawable.stock6, R.drawable.stock2, R.drawable.stock3, R.drawable.stock4, R.drawable.stock5, R.drawable.stock6)

        val gainerAdaptor = TopGainerAdaptor(ComSym3, ComName3, topImages3, Value3) // ✅ Fixed order
        binding.BottomSheetTopGainer.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.BottomSheetTopGainer.adapter = gainerAdaptor // ✅ Fixed RecyclerView binding


        return binding.root
    }

    companion object {


    }
}
package com.project.investgenius.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.investgenius.R
import com.project.investgenius.adaptor.TopGainerAdaptor
import com.project.investgenius.databinding.FragmentWatchlistBinding


class WatchlistFragment : Fragment() {
    private lateinit var binding: FragmentWatchlistBinding
    private lateinit var adpator: TopGainerAdaptor
    private val Originalstocknames = listOf("JSW Energy", "Hindalco", "Varun Beverages", "Tata Steel", "Macrotech Devis", "SBI Life Insurance", "Hindalco", "Varun Beverages", "Tata Steel", "Macrotech Devis", "SBI Life Insurance")
    private val OriginalValue3 = listOf("₹496.60", "₹653.55", "₹477.65", "₹140.76", "₹1,215.55", "₹1,495.40", "₹653.55", "₹477.65", "₹140.76", "₹1,215.55", "₹1,495.40")
    private val OriginalComSym3 = listOf("JSWENERGY", "HINDALCO", "VBL", "TATASTEEL", "LODHA", "SBILIFE", "HINDALCO", "VBL", "TATASTEEL", "LODHA", "SBILIFE")
    private val originaltopImages3 = listOf(R.drawable.stock1, R.drawable.stock2, R.drawable.stock3, R.drawable.stock4, R.drawable.stock5, R.drawable.stock6, R.drawable.stock2, R.drawable.stock3, R.drawable.stock4, R.drawable.stock5, R.drawable.stock6)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    private val filteredstocklistname = mutableListOf<String>()
    private val filteredstocklistSym = mutableListOf<String>()
    private val filteredstocklistPrice = mutableListOf<String>()
    private val filteredstocklistImag = mutableListOf<Int>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchlistBinding.inflate(inflater,container,false)
        adpator = TopGainerAdaptor(filteredstocklistname,filteredstocklistSym,filteredstocklistPrice,filteredstocklistImag)
        binding.BottomSheetTopGainer.layoutManager = LinearLayoutManager(requireContext())
        binding.BottomSheetTopGainer.adapter = adpator


        //setup searchview
        setupSearchView()
        //Show all stocks
        showAllStocks()

        return binding.root
    }

    private fun showAllStocks() {
        filteredstocklistname.clear()
        filteredstocklistSym.clear()
        filteredstocklistPrice.clear()
        filteredstocklistImag.clear()

        filteredstocklistname.addAll(Originalstocknames)
        filteredstocklistSym.addAll(OriginalComSym3)
        filteredstocklistPrice.addAll(OriginalValue3)
        filteredstocklistImag.addAll(originaltopImages3)

        adpator.notifyDataSetChanged()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterstocklist(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterstocklist(newText)
                return true
            }
        })
    }

    private fun filterstocklist(query: String) {
        filteredstocklistname.clear()
        filteredstocklistSym.clear()
        filteredstocklistPrice.clear()
        filteredstocklistImag.clear()


        Originalstocknames.forEachIndexed{index, stockname ->
            if (stockname.contains(query,ignoreCase = true)){
                filteredstocklistname.add(stockname)
                filteredstocklistSym.add(OriginalComSym3[index])
                filteredstocklistPrice.add(OriginalValue3[index])
                filteredstocklistImag.add(originaltopImages3[index])
            }
        }
        adpator.notifyDataSetChanged()

    }

    companion object {

    }
}
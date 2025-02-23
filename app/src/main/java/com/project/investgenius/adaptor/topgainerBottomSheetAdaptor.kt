package com.project.investgenius.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.investgenius.TopGainerBottomSheetFragment
import com.project.investgenius.databinding.ViewmoreTopgainerBinding


class topgainerBottomSheetAdaptor(
    private val companySymbol2: MutableList<String>,
    private val companyName2: MutableList<String>,
    private val image2: MutableList<Int>,
    private val value2: MutableList<String>
): RecyclerView.Adapter<topgainerBottomSheetAdaptor.GainerBottomSheetHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GainerBottomSheetHolder {
        val binding = ViewmoreTopgainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GainerBottomSheetHolder(binding)
    }



    override fun onBindViewHolder(holder: GainerBottomSheetHolder, position: Int) {
        val comSym2 = companySymbol2[position]
        val comName2 = companyName2[position]
        val valPrice2 = value2[position]
        val images2 = image2[position]
        holder.bind(comSym2, valPrice2, images2, comName2) // ✅ Removed .toString()
    }
    override fun getItemCount(): Int = companySymbol2.size
    inner class GainerBottomSheetHolder(private val binding: ViewmoreTopgainerBinding) :RecyclerView.ViewHolder(binding.root) {
        private val imagesView = binding.imageView15
        fun bind(comSym2: String, valPrice2: String, images2: Int, comName2: String) {
            binding.CompanySym.text = comSym2
            binding.companyname.text = comName2
            binding.prices.text = valPrice2
            imagesView.setImageResource(images2) // Ensure your layout has an ImageView with the correct ID
        }

    }

}
package com.project.investgenius.adaptor

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.investgenius.API.Gainer
import com.project.investgenius.databinding.ViewmoreTopgainerBinding

class TopGainerBottomSheetAdaptor(
    private var gainers: List<Gainer>
) : RecyclerView.Adapter<TopGainerBottomSheetAdaptor.GainerBottomSheetHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GainerBottomSheetHolder {
        val binding = ViewmoreTopgainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GainerBottomSheetHolder(binding)
    }

    override fun onBindViewHolder(holder: GainerBottomSheetHolder, position: Int) {
        val gainer = gainers[position]
        holder.bind(gainer)
    }

    override fun getItemCount(): Int = gainers.size

    fun updateData(newGainers: List<Gainer>) {
        gainers = newGainers
        notifyDataSetChanged()
    }

    fun getData(): List<Gainer> {
        return gainers
    }

    inner class GainerBottomSheetHolder(private val binding: ViewmoreTopgainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gainer: Gainer) {
            binding.CompanySym.text = gainer.symbol
            binding.companyname.text = gainer.company_name
            binding.prices.text = "₹${gainer.price}"

            val change = gainer.price_change
            binding.change.text = "$change%" // Show percentage change

            if (change >= 0) {
                binding.textView34.text = "^" // Up arrow for positive change
                binding.textView34.setTextColor(Color.parseColor("#4CAF50"))
                binding.textView34.setTextColor(Color.parseColor("#4CAF50"))
            } else {
                binding.textView34.text = "v" // Down arrow for negative change
            }
        }
    }
}

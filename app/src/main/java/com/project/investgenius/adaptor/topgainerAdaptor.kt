package com.project.investgenius.adaptor

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.investgenius.databinding.TopgainerslistBinding
import com.project.investgenius.API.Gainer
import com.bumptech.glide.Glide

class TopGainerAdaptor(
    private var topGainers: List<Gainer>
) : RecyclerView.Adapter<TopGainerAdaptor.TopGainerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopGainerViewHolder {
        return TopGainerViewHolder(
            TopgainerslistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TopGainerViewHolder, position: Int) {
        val gainer = topGainers[position]
        holder.bind(gainer)
    }

    override fun getItemCount(): Int {
        return topGainers.size
    }

    fun updateData(newGainers: List<Gainer>) {
        topGainers = newGainers
        notifyDataSetChanged()
    }

    // Corrected getter to return topGainers
    fun getData(): List<Gainer> {
        return topGainers
    }

    class TopGainerViewHolder(private val binding: TopgainerslistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gainer: Gainer) {
            binding.textView25.text = gainer.symbol
            binding.textView26.text = gainer.company_name
            binding.textView27.text = gainer.price.toString()

            // Optionally, load an image if you have one (using Glide, for example)
            // Glide.with(binding.imageView11.context).load(gainer.imageUrl).into(binding.imageView11)

            // Set the price change data into textView29 and textView32
            val change = gainer.price_change
            binding.textView29.text = "$change%" // Display the price change with a percentage sign

            if (change >= 0) {
                binding.textView32.text = "^" // Up arrow for positive change
                binding.textView29.setTextColor(Color.parseColor("#4CAF50"))
                binding.textView32.setTextColor(Color.parseColor("#4CAF50"))
            } else {
                binding.textView32.text = "v" // Down arrow for negative change
            }
        }
    }
}

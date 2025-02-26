package com.project.investgenius.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.investgenius.databinding.TopgainerslistBinding

class TopGainerAdaptor(
    private val companySymbol: List<String>,
    private val companyName: List<String>,
    private val value: List<String>, // Changed from Int to String
    private val image: List<Int> // Changed from String to Int
) : RecyclerView.Adapter<TopGainerAdaptor.TopGainerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopGainerViewHolder {
        return TopGainerViewHolder(
            TopgainerslistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TopGainerViewHolder, position: Int) {
        val comSym = companySymbol[position]
        val comName = companyName[position]
        val valPrice = value[position]
        val images = image[position]
        holder.bind(comSym, valPrice, images, comName)
    }

    override fun getItemCount(): Int {
        return companySymbol.size
    }

    class TopGainerViewHolder(private val binding: TopgainerslistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imagesView = binding.imageView11

        fun bind(comSym: String, valPrice: String, images: Int, comName: String) {
            binding.textView25.text = comSym
            binding.textView26.text = comName
            binding.textView27.text = valPrice
            imagesView.setImageResource(images)
        }
    }
}

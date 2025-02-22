package com.project.investgenius.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.investgenius.databinding.ActivityOtpBinding
import com.project.investgenius.databinding.IndicesBinding

class IndicesAdaptor ( private val indices:List<String>,private val values:List<Float>) : RecyclerView.Adapter<IndicesAdaptor.IndicesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndicesViewHolder {
        return IndicesViewHolder(IndicesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun onBindViewHolder(holder: IndicesViewHolder, position: Int) {
        val indice = indices[position]
        val value = values[position]
        holder.bind(indice,value)
    }
    override fun getItemCount(): Int {
        return indices.size
    }

    class IndicesViewHolder (private val binding: IndicesBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(indice: String, value: Float) {
            binding.textView23.text = indice
            binding.textView24.text = String.format("%.2f", value)
        }

    }
}
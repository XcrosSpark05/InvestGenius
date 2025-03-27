package com.project.investgenius.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.investgenius.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Move your UI-related logic inside onCreateView
        binding.editTextText2.isEnabled = false
        binding.editTextText3.isEnabled = false
        binding.editTextText10.isEnabled = false
        binding.editTextTextEmailAddress.isEnabled = false

        var isEnable = false
        binding.button9.setOnClickListener {
            isEnable = !isEnable
            binding.editTextText2.isEnabled = isEnable
            binding.editTextText3.isEnabled = isEnable
            binding.editTextText10.isEnabled = isEnable
            binding.editTextTextEmailAddress.isEnabled = isEnable
            if (isEnable) {
                binding.editTextText2.requestFocus()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }
}

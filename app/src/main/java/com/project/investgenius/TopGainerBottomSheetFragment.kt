package com.project.investgenius

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.investgenius.adaptor.TopGainerBottomSheetAdaptor
import com.project.investgenius.API.Gainer
import com.project.investgenius.databinding.FragmentTopGainerBottomSheetBinding

class TopGainerBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTopGainerBottomSheetBinding
    private lateinit var bottomSheetAdaptor: TopGainerBottomSheetAdaptor
    private var gainers: List<Gainer> = emptyList()

    companion object {
        private const val ARG_GAINERS = "arg_gainers"

        fun newInstance(gainers: ArrayList<Gainer>): TopGainerBottomSheetFragment {
            val fragment = TopGainerBottomSheetFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_GAINERS, gainers)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopGainerBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gainers = arguments?.getParcelableArrayList<Gainer>(ARG_GAINERS) ?: emptyList()
        bottomSheetAdaptor = TopGainerBottomSheetAdaptor(gainers)
        binding.BottomSheetTopGainer.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.BottomSheetTopGainer.adapter = bottomSheetAdaptor
    }
}

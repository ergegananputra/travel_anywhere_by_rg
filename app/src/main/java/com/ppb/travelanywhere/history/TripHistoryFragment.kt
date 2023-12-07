package com.ppb.travelanywhere.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ppb.travelanywhere.R
import com.ppb.travelanywhere.databinding.FragmentTripHistoryBinding


class TripHistoryFragment : Fragment() {

    private val binding by lazy { FragmentTripHistoryBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}
package com.ppb.travelanywhere.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ppb.travelanywhere.R
import com.ppb.travelanywhere.databinding.FragmentInformationManagerBinding


class InformationManagerFragment : Fragment() {

    private val binding by lazy {
        FragmentInformationManagerBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        floatingButton()
    }

    private fun floatingButton() {
        binding.floatingActionButtonAccounts.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}
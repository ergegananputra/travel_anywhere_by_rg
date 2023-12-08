package com.ppb.travelanywhere.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ppb.travelanywhere.databinding.FragmentProfileBinding
import com.ppb.travelanywhere.services.ApplicationPreferencesManager
import com.ppb.travelanywhere.services.api.FireAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileFragment : Fragment() {
    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonLogOut.setOnClickListener {
            Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show()
            logout()
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            val fireAuth = FireAuth()
            fireAuth.logout(ApplicationPreferencesManager(requireContext()).usernameId!!)
            ApplicationPreferencesManager(requireContext()).deleteUsername()
            requireActivity().finish()
        }

    }


}
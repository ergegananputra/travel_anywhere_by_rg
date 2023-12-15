package com.ppb.travelanywhere.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ppb.travelanywhere.R
import com.ppb.travelanywhere.SystemThemeViewModel
import com.ppb.travelanywhere.databinding.FragmentProfileBinding
import com.ppb.travelanywhere.services.ApplicationPreferencesManager
import com.ppb.travelanywhere.services.api.FireAuth
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    private val systemThemeViewModel: SystemThemeViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUsernameEmail()


        darkModeComponent()

        adminManager()


        binding.buttonLogOut.setOnClickListener {
            Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show()
            logout()
        }
    }

    private fun adminManager() {
        binding.containerKelolaAccount.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToManagerFragment()
            findNavController().navigate(action)

        }
    }

    private fun setUsernameEmail() {
        val preferences = ApplicationPreferencesManager(requireContext())
        binding.textViewUsername.text = preferences.usernameName
        binding.textViewNim.text = preferences.usernameNim

        if (preferences.isUserAdmin) {
            binding.containerKelolaAccount.visibility = View.VISIBLE
        } else {
            binding.containerKelolaAccount.visibility = View.GONE
        }
    }


    private fun darkModeComponent() {

        // Observe the theme mode
        systemThemeViewModel.switchState.value?.let { binding.switchThemeMode.isChecked = it }
        systemThemeViewModel.switchLabel.value?.let { binding.switchThemeMode.text = it }

        binding.switchThemeMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // The switch is checked, switch to dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchThemeMode.text = "Dark Mode"
            } else {
                // The switch is not checked, switch to light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchThemeMode.text = "Light Mode"
            }
            systemThemeViewModel.switchState.value = isChecked
            systemThemeViewModel.switchLabel.value = binding.switchThemeMode.text.toString()

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
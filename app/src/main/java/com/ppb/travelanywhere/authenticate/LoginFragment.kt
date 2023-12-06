package com.ppb.travelanywhere.authenticate

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ppb.travelanywhere.MainActivity
import com.ppb.travelanywhere.R
import com.ppb.travelanywhere.databinding.FragmentLoginBinding
import com.ppb.travelanywhere.services.ApplicationPreferencesManager
import com.ppb.travelanywhere.services.api.FireAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginFragment : Fragment() {

    private val binding by lazy{
        FragmentLoginBinding.inflate(layoutInflater)
    }

    private val fireAuth by lazy {
        FireAuth()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            editTextPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    containerEditTextPassword.error = null
                    containerEditTextPassword.isEndIconVisible = true
                }
            })

        }

        binding.buttonLogin.setOnClickListener {
            val inputUser = binding.editTextUsernameEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (inputUser.isEmpty()) {
                binding.containerEditTextUsernameEmail.error = "Mohon masukkan email atau username"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.containerEditTextPassword.error = "Mohon masukkan password"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                login(inputUser, password)
            }


        }


    }

    private suspend fun login(inputUser: String, password: String) {
        withContext(Dispatchers.IO) {
            if(fireAuth.login(inputUser, inputUser, password)) {
                ApplicationPreferencesManager(requireContext()).saveUsernameId(fireAuth.getUserId(inputUser, inputUser))
                val intentToMainActivity = Intent(requireContext(), MainActivity::class.java)
                startActivity(intentToMainActivity)
                requireActivity().finish()

            } else {
                Toast.makeText(requireContext(), "Username atau password salah", Toast.LENGTH_SHORT).show()

            }
        }

    }


}
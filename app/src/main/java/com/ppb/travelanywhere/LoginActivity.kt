package com.ppb.travelanywhere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ppb.travelanywhere.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            buttonLogin.setOnClickListener {
                if (editTextUsernameEmail.text.toString().isEmpty()) {
                    editTextUsernameEmail.error = "Mohon masukkan email"
                    return@setOnClickListener
                }
                if (editTextPassword.text.toString().isEmpty()) {
                    editTextPassword.error = "Mohon masukkan password"
                    return@setOnClickListener
                }

                // mengecek email username dan password
                if (editTextUsernameEmail.text.toString().isNotEmpty() && editTextPassword.text.toString().isNotEmpty() ) {

                }
            }
        }
    }
}
package com.ppb.travelanywhere

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.ppb.travelanywhere.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { _ ->

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            val username = intent.getStringExtra(RegisterActivity.EXTRA_USERNAME)
            val email = intent.getStringExtra(RegisterActivity.EXTRA_EMAIL)
            val password = intent.getStringExtra(RegisterActivity.EXTRA_PASSWORD)

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
                    if(editTextUsernameEmail.text.toString() == username || editTextUsernameEmail.text.toString() == email) {
                        if (editTextPassword.text.toString() == password) {
                            // login berhasil menuju ke MainActivity
                            val intentToMainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                            intentToMainActivity.putExtra(RegisterActivity.EXTRA_USERNAME, username)
                            setResult(Activity.RESULT_OK, intentToMainActivity)
                            launcher.launch(intentToMainActivity)


                        } else {
                            Toast.makeText(this@LoginActivity, "Password salah", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // username atau email salah
                        Toast.makeText(this@LoginActivity, "Username atau email salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
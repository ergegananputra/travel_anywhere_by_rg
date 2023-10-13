package com.ppb.travelanywhere



import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ppb.travelanywhere.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_PHONE = "extra_phone"
        const val EXTRA_PASSWORD = "extra_password"
    }

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            // Date of Birth
            buttonDatePicker.setOnClickListener {
                val datePicker = DatePicker()
                datePicker.show(supportFragmentManager, "date picker")
            }

            // Create Account
            buttonSignup.setOnClickListener {
                if (editTextEmail.text.toString().isEmpty()) {
                    editTextEmail.error = "Mohon masukkan email"
                    return@setOnClickListener
                }
                if (editTextUsernameEmail.text.toString().isEmpty()) {
                    editTextUsernameEmail.error = "Mohon masukkan username"
                    return@setOnClickListener
                }
                if (editTextDataOfBirth.text.toString().isEmpty()) {
                    editTextDataOfBirth.error = "Mohon masukkan tanggal lahir"
                    return@setOnClickListener
                }
                if (editTextPassword.text.toString().isEmpty()) {
                    editTextPassword.error = "Mohon masukkan password"
                    return@setOnClickListener
                }

                // Logika umur diatas 15 tahun aka gerbang create account
                if (editTextDataOfBirth.text.isNotEmpty()) {
                    //mendapatkan tahun sekarang
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

                    // kalkulasi umur
                    val dateOfBirth = editTextDataOfBirth.text.toString()
                    val dateOfBirthSplit = dateOfBirth.split("/")
                    val yearOfBirth = dateOfBirthSplit[2].toInt()
                    val age = currentYear - yearOfBirth
                    if (age < 15) {
                        editTextDataOfBirth.error = "Umur anda belum mencukupi"
                        return@setOnClickListener
                    }
                    else {
                        // Masuk ke login page
                        val intentToLoginPage = Intent(this@RegisterActivity, LoginActivity::class.java)

                        intentToLoginPage.putExtra(EXTRA_EMAIL, editTextEmail.text.toString())
                        editTextEmail.setText("")

                        intentToLoginPage.putExtra(EXTRA_USERNAME, editTextUsernameEmail.text.toString())
                        editTextUsernameEmail.setText("")

                        intentToLoginPage.putExtra(EXTRA_PASSWORD, editTextPassword.text.toString())
                        editTextPassword.setText("")

                        editTextDataOfBirth.setText("")

                        startActivity(intentToLoginPage)

                    }
                }
            }


        }
    }

    override fun onDateSet(
        view: android.widget.DatePicker?,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        // Ubah Edit Text
        val selectedDate="$dayOfMonth/${month+1}/$year"
        binding.editTextDataOfBirth.setText(selectedDate)
    }


}
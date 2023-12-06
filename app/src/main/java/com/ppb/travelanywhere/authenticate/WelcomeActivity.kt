package com.ppb.travelanywhere.authenticate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ppb.travelanywhere.MainActivity
import com.ppb.travelanywhere.R
import com.ppb.travelanywhere.databinding.ActivityWelcomeBinding
import com.ppb.travelanywhere.dialog.WelcomeSheetFragment
import com.ppb.travelanywhere.services.ApplicationPreferencesManager
import com.ppb.travelanywhere.services.api.FireAuth
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityWelcomeBinding.inflate(layoutInflater)
    }
    private val fireAuth by lazy {
        FireAuth()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        if (ApplicationPreferencesManager(this@WelcomeActivity).isUserIdNotNull) {
            lifecycleScope.launch {
                val isActive = fireAuth.checkUserStatus(ApplicationPreferencesManager(this@WelcomeActivity).usernameId!!)
                Log.w("WelcomeActivity", "onCreate: User is status : $isActive")
                if (isActive) {
                    val intentToMainActivity = Intent(this@WelcomeActivity, MainActivity::class.java)
                    startActivity(intentToMainActivity)
                    finish()
                }
                Log.d("WelcomeActivity", "onCreate: ${ApplicationPreferencesManager(this@WelcomeActivity).usernameId}")
            }
        }



        binding.buttonStart.setOnClickListener {
            WelcomeSheetFragment().show(supportFragmentManager, "WelcomeSheetFragment")

        }
    }

}
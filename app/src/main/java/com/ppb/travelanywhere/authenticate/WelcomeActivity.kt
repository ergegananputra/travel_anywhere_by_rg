package com.ppb.travelanywhere.authenticate

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
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

        val typedValue = TypedValue()
        theme.resolveAttribute(R.color.black, typedValue, true)
        val backgroundColor = typedValue.data
        window.statusBarColor = backgroundColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val attributes = window.attributes
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = attributes
        }


        if (ApplicationPreferencesManager(this@WelcomeActivity).isUserIdNotNull) {
            lifecycleScope.launch {
                val isActive = fireAuth.checkUserStatus(ApplicationPreferencesManager(this@WelcomeActivity).usernameId!!)
                Log.w("WelcomeActivity", "onCreate: User is status : $isActive")
                if (isActive) {
                    val intentToMainActivity = Intent(this@WelcomeActivity, MainActivity::class.java)
                    startActivity(intentToMainActivity)
//                    finish()
                }
                Log.d("WelcomeActivity", "onCreate: ${ApplicationPreferencesManager(this@WelcomeActivity).usernameId}")
            }
        }



        binding.buttonStart.setOnClickListener {
            WelcomeSheetFragment().show(supportFragmentManager, "WelcomeSheetFragment")

        }
    }

}
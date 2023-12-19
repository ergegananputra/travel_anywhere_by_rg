package com.ppb.travelanywhere.admin

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.WindowManager
import com.ppb.travelanywhere.R
import com.ppb.travelanywhere.databinding.ActivityAdminManagerBinding
import com.ppb.travelanywhere.databinding.FragmentManagerBinding

class AdminManagerActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAdminManagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val typedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true)
        val backgroundColor = typedValue.data
        window.statusBarColor = backgroundColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val attributes = window.attributes
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = attributes
        }


        binding.appHeader.buttonBack.visibility = android.view.View.VISIBLE
        binding.appHeader.buttonBack.setOnClickListener {
            finish()
        }
    }
}
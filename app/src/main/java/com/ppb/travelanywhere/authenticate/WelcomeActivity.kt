package com.ppb.travelanywhere.authenticate

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.getSystemService
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val POST_NOTIFICATIONS_PERMISSION = Manifest.permission.POST_NOTIFICATIONS
    private var POST_NOTIFICATIONS_GRANTED = false

    @RequiresApi(Build.VERSION_CODES.S)
    private val SCHEDULE_EXACT_ALARM_PERMISSION = Manifest.permission.SCHEDULE_EXACT_ALARM
    private var SCHEDULE_EXACT_ALARM_GRANTED = false


    private val launcherPermissionNotificationRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            POST_NOTIFICATIONS_GRANTED = true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            POST_NOTIFICATIONS_GRANTED = false
            showPermissionDialog("Notification Permission")
        }
    }

    private val launcherPermissionAlarmRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SCHEDULE_EXACT_ALARM_GRANTED = true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SCHEDULE_EXACT_ALARM_GRANTED = false
            showPermissionDialog("Schedule Alarm or Advance Alarm Notification")
        }
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
            attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = attributes
        }




        if (ApplicationPreferencesManager(this@WelcomeActivity).isUserIdNotNull) {
            lifecycleScope.launch {
                val isActive =
                    fireAuth.checkUserStatus(ApplicationPreferencesManager(this@WelcomeActivity).usernameId!!)
                Log.w("WelcomeActivity", "onCreate: User is status : $isActive")
                if (isActive) {
                    val intentToMainActivity =
                        Intent(this@WelcomeActivity, MainActivity::class.java)
                    startActivity(intentToMainActivity)
                    finish()
                }
                Log.d("WelcomeActivity", "onCreate: ${ApplicationPreferencesManager(this@WelcomeActivity).usernameId}"
                )
            }
        } else {
            ApplicationPreferencesManager(this@WelcomeActivity).deleteUsername()
        }

        // Permission Request
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!POST_NOTIFICATIONS_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Log.d("WelcomeActivity", "onCreate: Permission not granted")
                requestPermissionNotification()
            }

            val alarmManager = getSystemService<AlarmManager>()!!
            when {
                alarmManager.canScheduleExactAlarms() -> {
                    Log.d("WelcomeActivity", "onCreate: Permission granted")
                    SCHEDULE_EXACT_ALARM_GRANTED = true
                }
                else -> {
                    Log.d("WelcomeActivity", "onCreate: Permission not granted")
                    requestPermissionAlarm()
                }
            }

        }



        binding.buttonStart.setOnClickListener {
            Log.d("WelcomeActivity", "onCreate: Checking permission, status : \nNotification : $POST_NOTIFICATIONS_GRANTED \nAlarm : $SCHEDULE_EXACT_ALARM_GRANTED" )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val alarmManager = getSystemService<AlarmManager>()!!
                when {
                    alarmManager.canScheduleExactAlarms() -> {
                        Log.d("WelcomeActivity", "onCreate: Permission granted")
                        SCHEDULE_EXACT_ALARM_GRANTED = true
                    }
                }

                if (POST_NOTIFICATIONS_GRANTED && SCHEDULE_EXACT_ALARM_GRANTED) {
                    Log.d("WelcomeActivity", "onCreate: Permission granted")
                    WelcomeSheetFragment().show(supportFragmentManager, "WelcomeSheetFragment")
                } else if (SCHEDULE_EXACT_ALARM_GRANTED && Build.VERSION.SDK_INT == Build.VERSION_CODES.S) {
                    Log.d("WelcomeActivity", "onCreate: Permission granted")
                    WelcomeSheetFragment().show(supportFragmentManager, "WelcomeSheetFragment")
                }
            } else {
                WelcomeSheetFragment().show(supportFragmentManager, "WelcomeSheetFragment")
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestPermissionAlarm() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED) {
            SCHEDULE_EXACT_ALARM_GRANTED = true
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.SCHEDULE_EXACT_ALARM)) {
                Log.d("WelcomeActivity", "requestPermissionAlarm: Should show request permission rationale")
            } else {
                Log.d("WelcomeActivity", "requestPermissionAlarm: Should not show request permission rationale")
            }
            launcherPermissionAlarmRequest.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermissionNotification() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            POST_NOTIFICATIONS_GRANTED = true
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Log.d("WelcomeActivity", "requestPermissionNotification: Should show request permission rationale")
            } else {
                Log.d("WelcomeActivity", "requestPermissionNotification: Should not show request permission rationale")
            }
            launcherPermissionNotificationRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }



    @RequiresApi(Build.VERSION_CODES.S)
    private fun showPermissionDialog(permission_desc: String) {
        Log.d("WelcomeActivity", "showPermissionDialog: Showing permission dialog")
        AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setTitle("Alert for Permission")
            .setMessage("Permission $permission_desc is required to use this feature")
            .setPositiveButton("Go To System Settings") { dialog, _ ->
                startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                dialog.dismiss()
            }
            .setNegativeButton("Exit") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}



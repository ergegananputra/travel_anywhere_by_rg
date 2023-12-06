package com.ppb.travelanywhere.services

import android.content.Context
import android.icu.util.Calendar

class ApplicationPreferencesManager(context : Context) {
    private val sharedPreferences = context.getSharedPreferences("account_data", Context.MODE_PRIVATE)
    private val SPID : String = "username_id"

    val usernameId: String?
        get() = sharedPreferences.getString(SPID, null)
    val isUserIdNotNull : Boolean
        get() = sharedPreferences.getString(SPID, null) != null

    fun saveUsernameId(usernameId: String) {
        val expirationDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, 1)
        }.timeInMillis

        with(sharedPreferences.edit()) {
            putString(SPID, usernameId)
            putLong("username_expiration_date", expirationDate)
            apply()
        }
    }


    fun deleteUsername() {
        with(sharedPreferences.edit()) {
            remove(SPID)
            apply()
        }
    }
}
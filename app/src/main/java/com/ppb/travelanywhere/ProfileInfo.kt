package com.ppb.travelanywhere

import android.icu.util.Calendar

class ProfileInfo(
    username:String,
    email:String,
    password:String,
    dateOfBirth:String
) {
    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var dateOfBirth: String
    private lateinit var listTripPerjalanan: ArrayList<TripPerjalanan>

    init {
        createProfile(username, email, password, dateOfBirth)
    }

    fun createProfile(username: String, email: String, password: String, dateOfBirth: String) {
        this.username = username
        this.email = email
        this.password = password
        this.dateOfBirth = dateOfBirth
    }

    fun addTripPerjalanan(tripPerjalanan: TripPerjalanan) {
        listTripPerjalanan.add(tripPerjalanan)
    }

    fun anyMatchDateTripPerjalanan(dateTripPerjalanan: String): Boolean {
        for (tripPerjalanan in listTripPerjalanan) {
            if (tripPerjalanan.tanggalKeberangkatan == dateTripPerjalanan) {
                return true
            }
        }
        return false
    }




}
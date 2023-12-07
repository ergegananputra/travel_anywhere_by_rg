package com.ppb.travelanywhere.pesan

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.ppb.travelanywhere.databinding.ActivityPesanBinding

class PesanActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val binding by lazy {
        ActivityPesanBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(binding.root)
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Ubah Edit Text ke tanggal yang dipilih
        val selectedDate="$dayOfMonth/${month+1}/$year"
//        binding.editTextTanggalKeberangkatan.setText(selectedDate)
    }
}
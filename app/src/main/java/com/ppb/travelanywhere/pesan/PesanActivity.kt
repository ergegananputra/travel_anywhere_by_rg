package com.ppb.travelanywhere.pesan

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.ppb.travelanywhere.MainActivity.Companion.EXTRA_HARGA
import com.ppb.travelanywhere.MainActivity.Companion.EXTRA_KELAS_KERETA
import com.ppb.travelanywhere.MainActivity.Companion.EXTRA_NAMA_KERETA
import com.ppb.travelanywhere.MainActivity.Companion.EXTRA_PAKET_1
import com.ppb.travelanywhere.MainActivity.Companion.EXTRA_PAKET_2
import com.ppb.travelanywhere.MainActivity.Companion.EXTRA_PAKET_3
import com.ppb.travelanywhere.MainActivity.Companion.EXTRA_PAKET_4
import com.ppb.travelanywhere.MainActivity.Companion.EXTRA_PAKET_5
import com.ppb.travelanywhere.MainActivity.Companion.EXTRA_PAKET_6
import com.ppb.travelanywhere.MainActivity.Companion.EXTRA_PAKET_7
import com.ppb.travelanywhere.authenticate.RegisterActivity.Companion.EXTRA_USERNAME
import com.ppb.travelanywhere.MainActivity.Companion.EXTRA_STASIUN_KEBRANGKATAN
import com.ppb.travelanywhere.MainActivity.Companion.EXTRA_STASIUN_TUJUAN
import com.ppb.travelanywhere.MainActivity.Companion.EXTRA_TANGGAL_KEBERANGKATAN
import com.ppb.travelanywhere.R
import com.ppb.travelanywhere.authenticate.RegisterActivity
import com.ppb.travelanywhere.databinding.ActivityPesanBinding

class PesanActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityPesanBinding
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { _ ->
    }

    private var hargaTiketSementara = 0
    private var totalHarga = 0
    private var boolPaket1 = false
    private var boolPaket2 = false
    private var boolPaket3 = false
    private var boolPaket4 = false
    private var boolPaket5 = false
    private var boolPaket6 = false
    private var boolPaket7 = false


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPesanBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {

            // spinner stasiun keberangkatan
            val stasiun = resources.getStringArray(R.array.stasiun)
            spinnerStasiunKeberangkatan.adapter = ArrayAdapter<String> (
                this@PesanActivity,
                android.R.layout.simple_spinner_item,
                stasiun
            )

            // spinner stasiun tujuan
            spinnerStasiunTujuan.adapter = ArrayAdapter<String> (
                this@PesanActivity,
                android.R.layout.simple_spinner_item,
                stasiun
            )


            // Tanggal Keberangkatan
            buttonPilihTanggal.setOnClickListener {
                val datePickerDialog = com.ppb.travelanywhere.dialog.DatePicker()
                datePickerDialog.show(supportFragmentManager, "date picker")
            }

            // spinner pilih kereta
            val kereta = resources.getStringArray(R.array.nama_kereta)
            spinnerPilihKereta.adapter = ArrayAdapter<String> (
                this@PesanActivity,
                android.R.layout.simple_spinner_item,
                kereta
            )
            spinnerPilihKereta.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        hargaTiketSementara = (50000..200000).random()
                        totalHarga += hargaTiketSementara
                        textTotalHarga.text = "Rp${totalHarga}"

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        //TODO: Do something

                    }

                }

            // spinner pilih kelas
            val kelas = resources.getStringArray(R.array.kelas_kereta)
            spinnerPilihKelas.adapter = ArrayAdapter<String> (
                this@PesanActivity,
                android.R.layout.simple_spinner_item,
                kelas
            )
            spinnerPilihKelas.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        when(position) {
                            0 -> {
                                totalHarga = 0
                                textTotalHarga.text = "Rp${totalHarga}"
                            }
                            1 -> {
                                totalHarga = 150000 + hargaTiketSementara
                                textTotalHarga.text = "Rp${totalHarga}"
                            }
                            2 -> {
                                totalHarga = 50000 + hargaTiketSementara
                                textTotalHarga.text = "Rp${totalHarga}"
                            }
                            3 -> {
                                totalHarga = 0 + hargaTiketSementara
                                textTotalHarga.text = "Rp${totalHarga}"
                            }

                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        //TODO: Do something

                    }
                }


            // Paket
            toggleButtonPaket1.setOnClickListener() {
                if (toggleButtonPaket1.isChecked) {
                    totalHarga += 75000
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket1= true
                }
                else {
                    totalHarga -= 75000
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket1 = false
                }
            }
            toggleButtonPaket2.setOnClickListener() {
                if (toggleButtonPaket2.isChecked) {
                    totalHarga += 7500
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket2 = true
                }
                else {
                    totalHarga -= 7500
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket2 = false
                }
            }
            toggleButtonPaket3.setOnClickListener() {
                if (toggleButtonPaket3.isChecked) {
                    totalHarga += 30000
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket3 = true
                }
                else {
                    totalHarga -= 30000
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket3 = false
                }
            }
            toggleButtonPaket4.setOnClickListener() {
                if (toggleButtonPaket4.isChecked) {
                    totalHarga += 10000
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket4 = true
                }
                else {
                    totalHarga -= 10000
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket4 = false
                }
            }
            toggleButtonPaket5.setOnClickListener() {
                if (toggleButtonPaket5.isChecked) {
                    totalHarga += 20000
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket5 = true
                }
                else {
                    totalHarga -= 20000
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket5 = false
                }
            }
            toggleButtonPaket6.setOnClickListener() {
                if (toggleButtonPaket6.isChecked) {
                    totalHarga += 5000
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket6 = true
                }
                else {
                    totalHarga -= 5000
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket6 = false
                }
            }
            toggleButtonPaket7.setOnClickListener() {
                if (toggleButtonPaket7.isChecked) {
                    totalHarga -= 300000
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket7 = true
                }
                else {
                    totalHarga += 300000
                    textTotalHarga.text = "Rp${totalHarga}"
                    boolPaket7 = false
                }
            }

            // Button Pesan
            buttonCheckout.setOnClickListener() {
                val stasiunKeberangkatan = spinnerStasiunKeberangkatan.selectedItem.toString()
                val stasiunTujuan = spinnerStasiunTujuan.selectedItem.toString()
                val tanggalKeberangkatan = editTextTanggalKeberangkatan.text.toString()
                val namaKereta = spinnerPilihKereta.selectedItem.toString()
                val kelasKereta = spinnerPilihKelas.selectedItem.toString()

                if (
                    stasiunKeberangkatan != "Pilih"
                    && stasiunTujuan != "Pilih"
                    && tanggalKeberangkatan != ""
                    && namaKereta != "Pilih"
                    && kelasKereta != "Pilih"
                ) {



                    // Balik ke MainActivity
                    val username = intent.getStringExtra(RegisterActivity.EXTRA_USERNAME)
                    val intentToMainActivity = Intent()
                    intentToMainActivity.putExtra(EXTRA_USERNAME, username)
                    intentToMainActivity.putExtra(EXTRA_STASIUN_KEBRANGKATAN, stasiunKeberangkatan)
                    intentToMainActivity.putExtra(EXTRA_STASIUN_TUJUAN, stasiunTujuan)
                    intentToMainActivity.putExtra(EXTRA_TANGGAL_KEBERANGKATAN, tanggalKeberangkatan)
                    intentToMainActivity.putExtra(EXTRA_NAMA_KERETA, namaKereta)
                    intentToMainActivity.putExtra(EXTRA_KELAS_KERETA, kelasKereta)
                    intentToMainActivity.putExtra(EXTRA_PAKET_1, boolPaket1)
                    intentToMainActivity.putExtra(EXTRA_PAKET_2, boolPaket2)
                    intentToMainActivity.putExtra(EXTRA_PAKET_3, boolPaket3)
                    intentToMainActivity.putExtra(EXTRA_PAKET_4, boolPaket4)
                    intentToMainActivity.putExtra(EXTRA_PAKET_5, boolPaket5)
                    intentToMainActivity.putExtra(EXTRA_PAKET_6, boolPaket6)
                    intentToMainActivity.putExtra(EXTRA_PAKET_7, boolPaket7)
                    intentToMainActivity.putExtra(EXTRA_HARGA, totalHarga)

                    setResult(RESULT_OK, intentToMainActivity)
                    finish()

                } else {
                    // Tampilkan Toast
                    Toast.makeText(this@PesanActivity, "Mohon isi semua data", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Ubah Edit Text ke tanggal yang dipilih
        val selectedDate="$dayOfMonth/${month+1}/$year"
        binding.editTextTanggalKeberangkatan.setText(selectedDate)
    }
}
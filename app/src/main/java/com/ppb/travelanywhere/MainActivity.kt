package com.ppb.travelanywhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.ppb.travelanywhere.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val listTripTanggal = ArrayList<String>()

    companion object {
        const val EXTRA_STASIUN_KEBRANGKATAN = "extra_stasiun_keberangkatan"
        const val EXTRA_STASIUN_TUJUAN = "extra_stasiun_tujuan"
        const val EXTRA_TANGGAL_KEBERANGKATAN = "extra_tanggal_keberangkatan"
        const val EXTRA_NAMA_KERETA = "extra_nama_kereta"
        const val EXTRA_KELAS_KERETA = "extra_kelas_kereta"
        const val EXTRA_PAKET_1 = "extra_paket_1"
        const val EXTRA_PAKET_2 = "extra_paket_2"
        const val EXTRA_PAKET_3 = "extra_paket_3"
        const val EXTRA_PAKET_4 = "extra_paket_4"
        const val EXTRA_PAKET_5 = "extra_paket_5"
        const val EXTRA_PAKET_6 = "extra_paket_6"
        const val EXTRA_PAKET_7 = "extra_paket_7"
        const val EXTRA_HARGA = "extra_harga"
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data

            // dari login
            val username = data?.getStringExtra(RegisterActivity.EXTRA_USERNAME)

            // dari pesan
            val stasiunKeberangkatan = data?.getStringExtra(EXTRA_STASIUN_KEBRANGKATAN)
            val stasiunTujuan = data?.getStringExtra(EXTRA_STASIUN_TUJUAN)
            val tanggalKeberangkatan = data?.getStringExtra(EXTRA_TANGGAL_KEBERANGKATAN)
            val namaKereta = data?.getStringExtra(EXTRA_NAMA_KERETA)
            val kelasKereta = data?.getStringExtra(EXTRA_KELAS_KERETA)
            val paket1 = data?.getBooleanExtra(EXTRA_PAKET_1, false)
            val paket2 = data?.getBooleanExtra(EXTRA_PAKET_2, false)
            val paket3 = data?.getBooleanExtra(EXTRA_PAKET_3, false)
            val paket4 = data?.getBooleanExtra(EXTRA_PAKET_4, false)
            val paket5 = data?.getBooleanExtra(EXTRA_PAKET_5, false)
            val paket6 = data?.getBooleanExtra(EXTRA_PAKET_6, false)
            val paket7 = data?.getBooleanExtra(EXTRA_PAKET_7, false)

            val harga = data?.getIntExtra(EXTRA_HARGA, 0)

            // set up
            binding.textUsername.text = "Hi, ${username}!"

            if (stasiunKeberangkatan != null
                && stasiunTujuan != null
                && tanggalKeberangkatan != null
                && namaKereta != null
                && kelasKereta != null) {

                // Hilangkan Text Belum Ada Trip
                binding.textNoTripYet.visibility = View.GONE

                // Tampilkan Card
                binding.cardViewLastTrip.visibility = View.VISIBLE

                binding.textAsal.text = stasiunKeberangkatan
                binding.textTujuan.text = stasiunTujuan
                binding.textKeretaTanggal.text = tanggalKeberangkatan
                binding.textKeretaNama.text = namaKereta
                binding.textKeretaKelas.text = kelasKereta

                listTripTanggal.add(tanggalKeberangkatan)

                // Set Paket
                if (paket1 == true) {
                    binding.paket1.visibility = View.VISIBLE
                } else {
                    binding.paket1.visibility = View.GONE
                }

                if (paket2 == true) {
                    binding.paket2.visibility = View.VISIBLE
                } else {
                    binding.paket2.visibility = View.GONE
                }

                if (paket3 == true) {
                    binding.paket3.visibility = View.VISIBLE
                } else {
                    binding.paket3.visibility = View.GONE
                }

                if (paket4 == true) {
                    binding.paket4.visibility = View.VISIBLE
                } else {
                    binding.paket4.visibility = View.GONE
                }

                if (paket5 == true) {
                    binding.paket5.visibility = View.VISIBLE
                } else {
                    binding.paket5.visibility = View.GONE
                }

                if (paket6 == true) {
                    binding.paket6.visibility = View.VISIBLE
                } else {
                    binding.paket6.visibility = View.GONE
                }

                if (paket7 == true) {
                    binding.paket7.visibility = View.VISIBLE
                } else {
                    binding.paket7.visibility = View.GONE
                }

                // Set Harga
                binding.textTotalHarga.text = "Rp${harga},00"

            } else {
                // Menutup Card
                binding.cardViewLastTrip.visibility = View.GONE

                // Tampilkan Text Belum Ada Trip
                binding.textNoTripYet.visibility = View.VISIBLE
            }





        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){

            // set nama user
            val username = intent.getStringExtra(RegisterActivity.EXTRA_USERNAME)
            binding.textUsername.text = "Hi, ${username}!"

            // button pesan
            binding.buttonPesan.setOnClickListener {
                val intentToPesanActivity = Intent(this@MainActivity, PesanActivity::class.java)
                intentToPesanActivity.putExtra(RegisterActivity.EXTRA_USERNAME, username)
                launcher.launch(intentToPesanActivity)
            }

            // kalender
            binding.calendarView.setOnDateChangeListener() {
                    view,
                    year,
                    month,
                    dayOfMonth ->

                if (listTripTanggal.contains("$dayOfMonth/${month+1}/$year")) {
                    Toast.makeText(
                        this@MainActivity,
                        "Anda sudah pernah memesan pada tanggal $dayOfMonth/${month+1}/$year",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Anda belum pernah memesan pada tanggal $dayOfMonth/${month+1}/$year",
                        Toast.LENGTH_SHORT).show()
                }
            }




        }
    }
}
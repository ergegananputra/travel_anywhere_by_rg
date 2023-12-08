package com.ppb.travelanywhere.pesan

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ppb.travelanywhere.TravelAnywhereApps
import com.ppb.travelanywhere.databinding.ActivityPesanBinding
import com.ppb.travelanywhere.dialog.GlobalSheetFragment
import com.ppb.travelanywhere.dialog.adapter.GlobalTypeAdapter
import com.ppb.travelanywhere.dialog.viewmodel.GlobalViewModel
import com.ppb.travelanywhere.dialog.viewmodel.StationsKeberangkatanViewModel
import com.ppb.travelanywhere.dialog.viewmodel.StationsKedatanganViewModel
import com.ppb.travelanywhere.dialog.viewmodel.StationsViewModelFactory
import com.ppb.travelanywhere.dialog.viewmodel.TrainClassesViewModel
import com.ppb.travelanywhere.dialog.viewmodel.TrainClassesViewModelFactory
import com.ppb.travelanywhere.dialog.viewmodel.TrainsViewModel
import com.ppb.travelanywhere.dialog.viewmodel.TrainsViewModelFactory
import com.ppb.travelanywhere.services.calendar.CalendarModule
import com.ppb.travelanywhere.services.database.AppDatabaseViewModel
import com.ppb.travelanywhere.services.database.AppDatabaseViewModelFactory
import com.ppb.travelanywhere.services.database.DatabaseInformationManager
import com.ppb.travelanywhere.services.database.stations.StationsTable
import com.ppb.travelanywhere.services.database.train_classes.TrainClassesTable
import com.ppb.travelanywhere.services.database.trains.TrainsTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PesanActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val binding by lazy {
        ActivityPesanBinding.inflate(layoutInflater)
    }


    private val stasiunKeberangkatanViewModel by lazy {
        ViewModelProvider(this)[StationsKeberangkatanViewModel::class.java]
    }


    private val stasiunKedatanganViewModel by lazy {
        ViewModelProvider(this)[StationsKedatanganViewModel::class.java]
    }


    private val kelasKeretaViewModel by lazy {
        ViewModelProvider(this)[TrainClassesViewModel::class.java]
    }

    private val keretaViewModel by lazy {
        ViewModelProvider(this)[TrainsViewModel::class.java]
    }

    private lateinit var appViewModel : AppDatabaseViewModel

    private lateinit var listStasiun : List<StationsTable>
    private lateinit var listKelasKereta : List<TrainClassesTable>
    private lateinit var listKereta : List<TrainsTable>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fetchDataAndSetupSpinner()

        val typedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true)
        val backgroundColor = typedValue.data
        window.statusBarColor = backgroundColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val attributes = window.attributes
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = attributes
        }


        pilihTanggalKeberangkatan()




    }

    private fun fetchDataAndSetupSpinner() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val dBManager = DatabaseInformationManager(this@PesanActivity, application, this@PesanActivity)
                dBManager.checkAndUpdate()

                val factory = AppDatabaseViewModelFactory((application as TravelAnywhereApps).appRepository)
                appViewModel = ViewModelProvider(this@PesanActivity, factory)[AppDatabaseViewModel::class.java]

                listStasiun = appViewModel.listStations
                listKelasKereta = appViewModel.listTrainClasses
                listKereta = appViewModel.listTrains

                // Spinner
                withContext(Dispatchers.Main) {
                    setupSpinner()
                }

            }

        }

    }

    private fun setupSpinner() {
        stasiunKeberangkatan(listStasiun)
        stasiunKedatangan(listStasiun)
        kelasKereta(listKelasKereta)
        kereta(listKereta)
    }

    private fun kereta(list: List<TrainsTable>) {
        binding.textViewPilihanKereta.setOnClickListener {
            val bottomSheet = GlobalSheetFragment<TrainsTable>(
                title = "Pilih Kereta",
                globalAdapter = GlobalTypeAdapter<TrainsTable>(
                    list = list,
                    onClickItemListener = {},
                    textLabelLogic = { itemBinding, train ->
                        Log.d("PesanActivity", "Kereta: $train")
                        itemBinding.textViewOptionItem.text = train.name
                    }
                )
            )
            bottomSheet.globalAdapter.onClickItemListener = {
                Log.d("PesanActivity", "Kereta: $it")
                keretaViewModel.data.value = TrainsTable(
                    id = it.id,
                    name = it.name,
                    weight = it.weight
                )
                bottomSheet.dismiss()
            }
            bottomSheet.show(supportFragmentManager, "TrainsSheetFragment")
        }

        keretaViewModel.data.observe(this) {
            Log.d("PesanActivity", "Kereta: $it")
            binding.textViewPilihanKereta.text = it.name
        }
    }

    private fun kelasKereta(list: List<TrainClassesTable>) {
        binding.buttonPilihKelasKereta.setOnClickListener {
            val bottomSheet = GlobalSheetFragment<TrainClassesTable>(
                title = "Pilih Kelas Kereta",
                globalAdapter = GlobalTypeAdapter<TrainClassesTable>(
                    list = list,
                    onClickItemListener = {},
                    textLabelLogic = { itemBinding, trainClass ->
                        Log.d("PesanActivity", "Kelas Kereta: $trainClass")
                        itemBinding.textViewOptionItem.text = trainClass.name
                    }
                )
            )
                bottomSheet.globalAdapter.onClickItemListener = {
                    Log.d("PesanActivity", "Kelas Kereta: $it")
                    kelasKeretaViewModel.data.value = TrainClassesTable(
                        id = it.id,
                        name = it.name,
                        weight = it.weight
                    )
                    bottomSheet.dismiss()
                }
                bottomSheet.show(supportFragmentManager, "TrainClassesSheetFragment")
        }

        kelasKeretaViewModel.data.observe(this) {
            Log.d("PesanActivity", "Kelas Kereta: $it")
            binding.buttonPilihKelasKereta.text = it.name
        }
    }

    private fun stasiunKedatangan(list: List<StationsTable>) {
        binding.buttonPilihStasiunKedatangan.setOnClickListener {
            val bottomSheet = GlobalSheetFragment<StationsTable>(
                title = "Pilih Stasiun Kedatangan",
                globalAdapter = GlobalTypeAdapter<StationsTable>(
                    list = list,
                    onClickItemListener = {},
                    textLabelLogic = { itemBinding, station ->
                        Log.d("PesanActivity", "Stasiun Kedatangan: $station")
                        val option = "${station.city}, ${station.name}"
                        itemBinding.textViewOptionItem.text = option
                    }
                )
            )
            bottomSheet.globalAdapter.onClickItemListener = {
                Log.d("PesanActivity", "Stasiun Kedatangan: $it")
                stasiunKedatanganViewModel.data.value = StationsTable(
                    id = it.id,
                    name = it.name,
                    city = it.city,
                    weight = it.weight
                )
                bottomSheet.dismiss()
            }
            bottomSheet.show(supportFragmentManager, "StationsSheetFragment")
        }

        stasiunKedatanganViewModel.data.observe(this) {
            Log.d("PesanActivity", "Stasiun Kedatangan: $it")
            binding.buttonPilihStasiunKedatangan.text = it.name
        }
    }

    private fun stasiunKeberangkatan(list : List<StationsTable>) {
        binding.buttonPilihStasiunKeberangkatan.setOnClickListener {
            val bottomSheet = GlobalSheetFragment<StationsTable>(
                title = "Pilih Stasiun Keberangkatan",
                globalAdapter = GlobalTypeAdapter<StationsTable>(
                    list = list,
                    onClickItemListener = {},
                    textLabelLogic = { itemBinding, station ->
                        Log.d("PesanActivity", "Stasiun Kedatangan: $station")
                        val option = "${station.city}, ${station.name}"
                        itemBinding.textViewOptionItem.text = option
                    }
                )
            )
            bottomSheet.globalAdapter.onClickItemListener = {
                Log.d("PesanActivity", "Stasiun Keberangkatan: $it")
                stasiunKeberangkatanViewModel.data.value = StationsTable(
                    id = it.id,
                    name = it.name,
                    city = it.city,
                    weight = it.weight
                )
                bottomSheet.dismiss()
            }
            bottomSheet.show(supportFragmentManager, "StationsSheetFragment")
        }

        stasiunKeberangkatanViewModel.data.observe(this) {
            Log.d("PesanActivity", "Stasiun Keberangkatan: $it")
            binding.buttonPilihStasiunKeberangkatan.text = it.name
        }
    }

    private fun pilihTanggalKeberangkatan() {
        binding.buttonPilihTanggal.setOnClickListener {
            val datePickerDialog = com.ppb.travelanywhere.dialog.DatePicker()
            datePickerDialog.show(supportFragmentManager, "DatePicker")

        }
    }


    override fun onDateSet(
        view: DatePicker?,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        Log.d("PesanActivity", "onDateSet: $dayOfMonth ${month+1} $year")
        val interval = CalendarModule.getIntervalDay(dayOfMonth, month, year)
        if (interval < 0) {
            Log.w("PesanActivity", "onDateSet: Invalid date input $dayOfMonth ${month+1} $year")
            Toast.makeText(this, "Tanggal tidak valid", Toast.LENGTH_SHORT).show()
            return
        } else if (interval > 400) {
            Log.w("PesanActivity", "onDateSet: Invalid date input $dayOfMonth ${month+1} $year")
            Toast.makeText(this, "Tanggal tidak valid : Terlalu Lama", Toast.LENGTH_SHORT).show()
            return
        }

        val months = resources.getStringArray(com.ppb.travelanywhere.R.array.months)
        val selectedDate="$dayOfMonth ${months[month]} $year"

        val getTailText = binding.countTanggalKeberangkatan.text.toString().split(" ")
        var intervalText = interval.toString()
        getTailText.forEach {
            if (getTailText.indexOf(it) != 0) {
                intervalText += " $it"
            }
        }

        binding.textViewTanggalKeberangkatan.text = selectedDate
        binding.countTanggalKeberangkatan.text = intervalText

    }
}
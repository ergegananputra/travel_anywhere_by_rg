package com.ppb.travelanywhere.admin

import android.os.Bundle
import android.text.TextUtils.split
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.ppb.travelanywhere.R
import com.ppb.travelanywhere.TravelAnywhereApps
import com.ppb.travelanywhere.databinding.FragmentInformationManagerBinding
import com.ppb.travelanywhere.dialog.GlobalSheetFragment
import com.ppb.travelanywhere.dialog.adapter.GlobalTypeAdapter
import com.ppb.travelanywhere.services.database.AppDatabaseViewModel
import com.ppb.travelanywhere.services.database.AppDatabaseViewModelFactory
import com.ppb.travelanywhere.services.database.DatabaseInformationManager
import com.ppb.travelanywhere.services.database.stations.StationsTable
import com.ppb.travelanywhere.services.database.train_classes.TrainClassesTable
import com.ppb.travelanywhere.services.database.trains.TrainsTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class InformationManagerFragment : Fragment() {

    private val binding by lazy {
        FragmentInformationManagerBinding.inflate(layoutInflater)
    }
    private val informationViewModel : InformationViewModel by viewModels()
    private lateinit var appViewModel : AppDatabaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = AppDatabaseViewModelFactory((requireActivity().application as TravelAnywhereApps).appRepository)
        appViewModel = ViewModelProvider(requireActivity(), factory)[AppDatabaseViewModel::class.java]

        floatingButton()
        pilihTipeDatabase()

        setupSwipeRefresh()
        informationCard()

        binding.buttonHapusData.setOnClickListener {
            deleteData()
        }

        binding.buttonAmbilData.setOnClickListener {
            clearFields()

            if (GlobalSheetFragment.isDialogOpen) {
                return@setOnClickListener
            }

            retrieveData()
        }

        binding.buttonSimpanData.setOnClickListener {
            uploadData()
        }

        binding.buttonReset.setOnClickListener {
            clearFields()
        }
    }

    private fun informationCard() {
        binding.materialCardViewInformation.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.constraintLayoutInformationManager)
            if (binding.textViewManagerDescription.visibility == View.GONE) {
                binding.textViewManagerDescription.visibility = View.VISIBLE
            } else {
                binding.textViewManagerDescription.visibility = View.GONE
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayoutInformationManager.setOnRefreshListener {
            lifecycleScope.launch {
                updateData()
                val currentQueue = appViewModel.countQueue()
                Log.d("InformationManagerFragment", "setupSwipeRefresh: $currentQueue")
                if (currentQueue > 0 ) {
                    val activity = requireActivity() as AppCompatActivity
                    val dBManager = DatabaseInformationManager(requireActivity(), requireActivity().application, activity)
                    dBManager.sendQueue()
                }
                binding.countLogTextView.text = currentQueue.toString()
                binding.swipeRefreshLayoutInformationManager.isRefreshing = false
            }
        }
    }

    private suspend fun updateData() {
        val activity = requireActivity() as AppCompatActivity
        val dBManager = DatabaseInformationManager(requireActivity(), requireActivity().application, activity)
        dBManager.checkAndUpdate()
    }

    private fun deleteData() {
        if (informationViewModel.isUpdate && informationViewModel.targetId != null) {
            uploadData(targetAction = "delete")
        }
    }

    private fun retrieveData() {

        lifecycleScope.launch {
            val optionDb = informationViewModel.getOption()
            if (optionDb == 0) {
                val list = appViewModel.listStations()

                val bottomSheet = GlobalSheetFragment<StationsTable>(
                    title = "Pilih Stasiun Keberangkatan",
                    globalAdapter = GlobalTypeAdapter<StationsTable>(
                        list = list.toMutableList(),
                        onClickItemListener = {},
                        textLabelLogic = { itemBinding, station ->
                            Log.d("InformationManagerFragment", "Stasiun Kedatangan: $station")
                            val option = "${station.city}, ${station.name}"
                            itemBinding.textViewOptionItem.text = option
                        }
                    ),
                    searchFeatures = true,
                )
                bottomSheet.globalAdapter.onClickItemListener = {
                    Log.d("InformationManagerFragment", "Stasiun Keberangkatan: $it")

                    informationViewModel.optionNameValue = it.name
                    informationViewModel.optionWeightValue = it.weight.toString()
                    informationViewModel.optionCityValue = it.city

                    binding.nameInputEditText.setText(informationViewModel.optionNameValue)
                    binding.cityInputEditText.setText(informationViewModel.optionCityValue)
                    binding.weightInputEditText.setText(informationViewModel.optionWeightValue)
                    informationViewModel.isUpdate = true
                    informationViewModel.targetId = it.id

                    bottomSheet.dismiss()
                    changeIconToEditMode()
                }
                bottomSheet.searchLogic = { query ->
                    lifecycleScope.launch {
                        val results = appViewModel.searchStations(query)
                        bottomSheet.globalAdapter.updateList(results)
                    }
                }

                bottomSheet.show(requireActivity().supportFragmentManager, "StationsSheetFragment")


            } else if (optionDb == 1) {
                val list = appViewModel.listTrains()

                val bottomSheet = GlobalSheetFragment<TrainsTable>(
                    title = "Pilih Kereta",
                    globalAdapter = GlobalTypeAdapter<TrainsTable>(
                        list = list.toMutableList(),
                        onClickItemListener = {},
                        textLabelLogic = { itemBinding, train ->
                            Log.d("InformationManagerFragment", "Kereta: $train")
                            itemBinding.textViewOptionItem.text = train.name
                        }
                    ),
                    searchFeatures = true,
                )
                bottomSheet.globalAdapter.onClickItemListener = {
                    Log.d("InformationManagerFragment", "Kereta: $it")

                    informationViewModel.optionNameValue = it.name
                    informationViewModel.optionWeightValue = it.weight.toString()

                    binding.nameInputEditText.setText(informationViewModel.optionNameValue)
                    binding.weightInputEditText.setText(informationViewModel.optionWeightValue)
                    informationViewModel.isUpdate = true
                    informationViewModel.targetId = it.id

                    bottomSheet.dismiss()
                    changeIconToEditMode()
                }
                bottomSheet.searchLogic = { query ->
                    lifecycleScope.launch {
                        val results = appViewModel.searchTrains(query)
                        bottomSheet.globalAdapter.updateList(results)
                    }
                }

                bottomSheet.show(requireActivity().supportFragmentManager, "TrainsSheetFragment")

            } else if (optionDb == 2) {
                val list = appViewModel.listTrainClasses()

                val bottomSheet = GlobalSheetFragment<TrainClassesTable>(
                    title = "Pilih Kelas Kereta",
                    globalAdapter = GlobalTypeAdapter<TrainClassesTable>(
                        list = list.toMutableList(),
                        onClickItemListener = {},
                        textLabelLogic = { itemBinding, trainClass ->
                            Log.d("InformationManagerFragment", "Kelas Kereta: $trainClass")
                            itemBinding.textViewOptionItem.text = trainClass.name
                        }
                    ),
                    searchFeatures = true,
                )
                bottomSheet.globalAdapter.onClickItemListener = {
                    Log.d("InformationManagerFragment", "Kelas Kereta: $it")

                    informationViewModel.optionNameValue = it.name
                    informationViewModel.optionWeightValue = it.weight.toString()

                    binding.nameInputEditText.setText(informationViewModel.optionNameValue)
                    binding.weightInputEditText.setText(informationViewModel.optionWeightValue)
                    informationViewModel.isUpdate = true
                    informationViewModel.targetId = it.id

                    bottomSheet.dismiss()
                    changeIconToEditMode()
                }
                bottomSheet.searchLogic = { query ->
                    lifecycleScope.launch {
                        val results = appViewModel.searchTrainClasses(query)
                        bottomSheet.globalAdapter.updateList(results)
                    }
                }

                bottomSheet.show(requireActivity().supportFragmentManager, "TrainClassesSheetFragment")
            }
        }

    }

    private fun changeIconToEditMode() {
        binding.buttonSimpanData.setImageResource(R.drawable.edit_fill1_wght400_grad0_opsz24)
    }

    private fun changeIconToInsertMode() {
        binding.buttonSimpanData.setImageResource(R.drawable.baseline_cloud_upload_24)
    }

    private fun uploadData(targetAction : String = "update") {
        lifecycleScope.launch {
            val option = informationViewModel.getOption()

            if (option == 0) {
                if (binding.cityInputEditText.text.toString().isEmpty()) {
                    binding.cityInputLayout.error = "Kota tidak boleh kosong"
                    return@launch
                }
            }

            if (binding.nameInputEditText.text.toString().isEmpty()) {
                binding.nameInputLayout.error = "Nama tidak boleh kosong"
                return@launch
            }
            if (binding.weightInputEditText.text.toString().isEmpty()) {
                binding.weightInputLayout.error = "Berat tidak boleh kosong"
                return@launch
            }


            val city = if (informationViewModel.getOption() == 1) {
                val temp = binding.cityInputEditText.text.toString().trim().split(" ").joinToString(" ") { it -> it.replaceFirstChar { it.uppercase() }}
                if (temp.startsWith("Stasiun") || temp.startsWith("Station") || temp.startsWith("St")) {
                    temp
                } else {
                    "Stasiun $temp"
                }
            } else {
                binding.cityInputEditText.text.toString().trim().split(" ").joinToString(" ") { it -> it.replaceFirstChar { it.uppercase() }}
            }

            if (informationViewModel.isUpdate) {

                appViewModel.insertQueue(
                    targetTable = binding.buttonPilihTipeDatabase.text.toString(),
                    targetAction = targetAction,
                    idTarget = informationViewModel.targetId!!,
                    name = binding.nameInputEditText.text.toString().trim().split(" ").joinToString(" ") { it -> it.replaceFirstChar { it.uppercase() }},
                    weight = binding.weightInputEditText.text.toString().toInt(),
                    additionalData = city
                )
            } else {
                appViewModel.insertQueue(
                    targetTable = binding.buttonPilihTipeDatabase.text.toString(),
                    targetAction = "insert",
                    idTarget = "",
                    name = binding.nameInputEditText.text.toString().trim().split(" ").joinToString(" ") { it -> it.replaceFirstChar { it.uppercase() }},
                    weight = binding.weightInputEditText.text.toString().toInt(),
                    additionalData = city
                )
            }

            var currentQueue = appViewModel.countQueue()
            binding.countLogTextView.text = currentQueue.toString()

            val activity = requireActivity() as AppCompatActivity
            val dBManager = DatabaseInformationManager(requireActivity(), requireActivity().application, activity)

            dBManager.sendQueue()
            clearFields()

            withContext(Dispatchers.IO) {
                delay(5000L)
                currentQueue = appViewModel.countQueue()
                if (currentQueue == 0 ) {
                    updateData()
                }
                withContext(Dispatchers.Main) {
                    binding.countLogTextView.text = currentQueue.toString()
                }
            }

        }
    }

    private fun pilihTipeDatabase() {
        binding.buttonPilihTipeDatabase.setOnClickListener {
            lifecycleScope.launch {
                val newLabel = informationViewModel.nextOptionString()
                binding.buttonPilihTipeDatabase.text = newLabel
            }
        }

        informationViewModel.option.observe(viewLifecycleOwner) {
            clearFields()
            when (it) {
                0 -> {
                    binding.cityInputLayout.visibility = View.VISIBLE
                }
                1 -> {
                    binding.cityInputLayout.visibility = View.GONE
                }
                2 -> {
                    binding.cityInputLayout.visibility = View.GONE
                }
            }
        }
    }

    private fun clearFields() {
        binding.nameInputEditText.setText("")
        binding.weightInputEditText.setText("")
        binding.cityInputEditText.setText("")

        binding.nameInputEditText.clearFocus()
        binding.weightInputEditText.clearFocus()
        binding.cityInputEditText.clearFocus()

        binding.nameInputEditText.error = null
        binding.weightInputEditText.error = null
        binding.cityInputEditText.error = null

        informationViewModel.isUpdate = false
        informationViewModel.targetId = null

        informationViewModel.optionNameValue = null
        informationViewModel.optionWeightValue = null
        informationViewModel.optionCityValue = null

        changeIconToInsertMode()
    }

    private fun floatingButton() {
        binding.floatingActionButtonAccounts.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}
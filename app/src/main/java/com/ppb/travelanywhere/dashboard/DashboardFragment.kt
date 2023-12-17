package com.ppb.travelanywhere.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ppb.travelanywhere.R
import com.ppb.travelanywhere.TravelAnywhereApps
import com.ppb.travelanywhere.databinding.FragmentDashboardBinding
import com.ppb.travelanywhere.pesan.PesanActivity
import com.ppb.travelanywhere.services.ApplicationPreferencesManager
import com.ppb.travelanywhere.services.calendar.CalendarModule
import com.ppb.travelanywhere.services.database.AppDatabaseViewModel
import com.ppb.travelanywhere.services.database.AppDatabaseViewModelFactory
import com.ppb.travelanywhere.services.database.ticket_history.TicketHistoryTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class DashboardFragment : Fragment() {

    private val binding by lazy { FragmentDashboardBinding.inflate(layoutInflater) }
    private lateinit var appViewModel : AppDatabaseViewModel

    private val launcherToPesan = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("DashboardFragment", "Result OK :Back From PesanFragment")
            refreshPage()

        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            Log.d("DashboardFragment", "Result Canceled :Back From PesanFragment")
        } else {
            Log.d("DashboardFragment", "Result Unknown :Back From PesanFragment")
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = AppDatabaseViewModelFactory((requireActivity().application as TravelAnywhereApps).appRepository)
        appViewModel = ViewModelProvider(requireActivity(), factory)[AppDatabaseViewModel::class.java]


        marqueeSupport()
        setUsername()

        setFloatingActionButton()

        refreshPage()



    }

    private fun marqueeSupport() {
        binding.textUsername.isSelected = true
    }



    private fun setFloatingActionButton() {
        Log.d("DashboardFragment", "setFloatingActionButton Generated")

        binding.nestedScrollViewDashboard.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY && binding.floatingActionButtonPesan.isExtended) {
                binding.floatingActionButtonPesan.shrink()
            } else if (scrollY < oldScrollY && !binding.floatingActionButtonPesan.isExtended) {
                binding.floatingActionButtonPesan.extend()
            }
        })


        binding.floatingActionButtonPesan.setOnClickListener {
            Log.d("DashboardFragment", "FAB navigate to PesanFragment")
            val intentToPesan = Intent(context, PesanActivity::class.java)
            launcherToPesan.launch(intentToPesan)
        }
    }

    private fun setUsername() {
        val preferences = ApplicationPreferencesManager(requireContext())
        Log.d("DashboardFragment", "setUsername: ${preferences.usernameName}")
        val username = preferences.usernameName ?: "Welcome to Travel Anywhere"
        val greeting = "Hi, $username !"
        binding.textUsername.text = greeting
    }

    private fun refreshPage() {
        loadUpCommingTrip()
        loadLatestTrip()
    }

    private fun loadLatestTrip() {
        lifecycleScope.launch {
            Log.d("DashboardFragment", "loadLatestTrip: start")
            val latestTrip = withContext(Dispatchers.IO) {
                appViewModel.listTicketHistory()?.firstOrNull()
            }

            if (latestTrip != null) {
                setUpCardLatestTrip(latestTrip)
            } else {
                val tripCard = binding.containerLastTrip
                tripCard.theCard.visibility = View.GONE
            }
        }
    }

    private fun setUpCardLatestTrip(ticketHistoryTable: TicketHistoryTable) {
        Log.d("DashboardFragment", "setUpCardLatestTrip: $ticketHistoryTable")

        val tripCard = binding.containerLastTrip

        tripCard.theCard.visibility = View.VISIBLE
        tripCard.buttonActive.visibility = View.VISIBLE

        val calendar = Calendar.getInstance()
        calendar.time = ticketHistoryTable.tanggalKeberangkatan!!

        var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        var month = calendar.get(Calendar.MONTH)
        var year = calendar.get(Calendar.YEAR)

        val intervalCheck = CalendarModule.getIntervalDay(dayOfMonth, month, year)
        if (intervalCheck > 0) {
            tripCard.buttonProcess.visibility = View.GONE
            tripCard.buttonActive.visibility = View.VISIBLE
        } else {
            tripCard.buttonProcess.visibility = View.VISIBLE
            tripCard.buttonActive.visibility = View.GONE
        }


        setDate(ticketHistoryTable.tanggalKeberangkatan, tripCard.textKeretaTanggal)
        tripCard.textKeretaNama.text = ticketHistoryTable.kereta
        tripCard.textKeretaKelas.text = ticketHistoryTable.kelas
        tripCard.textTotalHarga.text = "Rp${ticketHistoryTable.harga.toString()}"

        tripCard.kotaAsal.text = ticketHistoryTable.kotaAsal
        tripCard.kotaTujuan.text = ticketHistoryTable.kotaTujuan
        tripCard.stasiunAsal.text = ticketHistoryTable.stasiunAsal
        tripCard.stasiunTujuan.text = ticketHistoryTable.stasiunTujuan


        ticketHistoryTable.paketBinary.forEachIndexed { index, digit ->
            if (digit == '0') {
                when(index) {
                    0 -> tripCard.paket1.visibility = View.GONE
                    1 -> tripCard.paket2.visibility = View.GONE
                    2 -> tripCard.paket3.visibility = View.GONE
                    3 -> tripCard.paket4.visibility = View.GONE
                    4 -> tripCard.paket5.visibility = View.GONE
                    5 -> tripCard.paket6.visibility = View.GONE
                    6 -> tripCard.paket7.visibility = View.GONE
                }
            } else {
                when(index) {
                    0 -> tripCard.paket1.visibility = View.VISIBLE
                    1 -> tripCard.paket2.visibility = View.VISIBLE
                    2 -> tripCard.paket3.visibility = View.VISIBLE
                    3 -> tripCard.paket4.visibility = View.VISIBLE
                    4 -> tripCard.paket5.visibility = View.VISIBLE
                    5 -> tripCard.paket6.visibility = View.VISIBLE
                    6 -> tripCard.paket7.visibility = View.VISIBLE
                }
            }
        }

        tripCard.buttonActive.setOnClickListener {

            calendar.time = ticketHistoryTable.tanggalKeberangkatan!!

            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)

            val interval = CalendarModule.getIntervalDay(dayOfMonth, month, year)
            Toast.makeText(requireContext(), "Perjalanan dimulai $interval hari lagi", Toast.LENGTH_SHORT).show()
        }

    }

    private fun loadUpCommingTrip() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val upcomingTrip = appViewModel.upComingTicketHistory()

                if (upcomingTrip != null) {
                    setUpCardUpcomingTrip(upcomingTrip)
                } else {
                    val tripCard = binding.containerNextTrip
                    tripCard.theCard.visibility = View.GONE
                }
            }
        }
    }

    private suspend fun setUpCardUpcomingTrip(ticketHistoryTable: TicketHistoryTable) {
        Log.d("DashboardFragment", "setUpCardUpcomingTrip: $ticketHistoryTable")
        withContext(Dispatchers.Main) {
            val tripCard = binding.containerNextTrip

            tripCard.theCard.visibility = View.VISIBLE
            tripCard.buttonActive.visibility = View.VISIBLE
            tripCard.buttonProcess.visibility = View.GONE
            setDate(ticketHistoryTable.tanggalKeberangkatan, tripCard.textKeretaTanggal)
            tripCard.textKeretaNama.text = ticketHistoryTable.kereta
            tripCard.textKeretaKelas.text = ticketHistoryTable.kelas
            tripCard.textTotalHarga.text = "Rp${ticketHistoryTable.harga.toString()}"

            tripCard.kotaAsal.text = ticketHistoryTable.kotaAsal
            tripCard.kotaTujuan.text = ticketHistoryTable.kotaTujuan
            tripCard.stasiunAsal.text = ticketHistoryTable.stasiunAsal
            tripCard.stasiunTujuan.text = ticketHistoryTable.stasiunTujuan


            ticketHistoryTable.paketBinary.forEachIndexed { index, digit ->
                if (digit == '0') {
                    when(index) {
                        0 -> tripCard.paket1.visibility = View.GONE
                        1 -> tripCard.paket2.visibility = View.GONE
                        2 -> tripCard.paket3.visibility = View.GONE
                        3 -> tripCard.paket4.visibility = View.GONE
                        4 -> tripCard.paket5.visibility = View.GONE
                        5 -> tripCard.paket6.visibility = View.GONE
                        6 -> tripCard.paket7.visibility = View.GONE
                    }
                } else {
                    when(index) {
                        0 -> tripCard.paket1.visibility = View.VISIBLE
                        1 -> tripCard.paket2.visibility = View.VISIBLE
                        2 -> tripCard.paket3.visibility = View.VISIBLE
                        3 -> tripCard.paket4.visibility = View.VISIBLE
                        4 -> tripCard.paket5.visibility = View.VISIBLE
                        5 -> tripCard.paket6.visibility = View.VISIBLE
                        6 -> tripCard.paket7.visibility = View.VISIBLE
                    }
                }
            }

            tripCard.buttonActive.setOnClickListener {
                val calendar = Calendar.getInstance()
                calendar.time = ticketHistoryTable.tanggalKeberangkatan!!

                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)

                val interval = CalendarModule.getIntervalDay(dayOfMonth, month, year)
                Toast.makeText(requireContext(), "Perjalanan dimulai $interval hari lagi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setDate(date: Date?, view: TextView) {
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date

            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val months = resources.getStringArray(R.array.months)

            val theDate = "$dayOfMonth ${months[month]} $year"
            view.text =  theDate
        }
    }


}
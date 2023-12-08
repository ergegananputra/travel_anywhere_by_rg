package com.ppb.travelanywhere.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ppb.travelanywhere.R
import com.ppb.travelanywhere.databinding.FragmentDashboardBinding
import com.ppb.travelanywhere.pesan.PesanActivity

class DashboardFragment : Fragment() {

    private val binding by lazy { FragmentDashboardBinding.inflate(layoutInflater) }

    private val launcherToPesan = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("DashboardFragment", "Result OK :Back From PesanFragment")


            //TODO: Tambahkan kode untuk refresh data di DashboardFragment


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
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setFloatingActionButton()

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


}
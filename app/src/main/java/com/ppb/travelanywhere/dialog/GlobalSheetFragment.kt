package com.ppb.travelanywhere.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ppb.travelanywhere.databinding.FragmentBottomSheetDialogBinding
import com.ppb.travelanywhere.dialog.adapter.GlobalTypeAdapter
import com.ppb.travelanywhere.dialog.viewmodel.GlobalViewModel

class GlobalSheetFragment<T>(
    private val title : String,
    val globalAdapter: GlobalTypeAdapter<T>
) : BottomSheetDialogFragment() {

    private val binding by lazy { FragmentBottomSheetDialogBinding.inflate(layoutInflater)}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewTitleBottomSheet.text = title

        setupRecycler()
    }

    private fun setupRecycler() {
        with(binding) {
            recyclerViewSheet.apply {
                adapter = globalAdapter
                layoutManager = LinearLayoutManager(requireActivity())
            }
        }
    }

    fun closeDialog() {
        dismiss()
    }

}
package com.ppb.travelanywhere.dialog

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ppb.travelanywhere.databinding.FragmentBottomSheetDialogBinding
import com.ppb.travelanywhere.dialog.adapter.GlobalTypeAdapter
import com.ppb.travelanywhere.dialog.viewmodel.GlobalViewModel

class GlobalSheetFragment<T>(
    private val title : String,
    val globalAdapter: GlobalTypeAdapter<T>,
    private val searchFeatures: Boolean = false,
    var searchLogic : (String) -> Unit = {}
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

        if (searchFeatures) {
            binding.textEditSearchSheet.visibility = View.VISIBLE

            binding.textEditSearchSheet.doAfterTextChanged {
                val searchQuery = it.toString().trim()

                searchLogic(searchQuery)
            }

            binding.textInputLayoutSearchSheet.setEndIconOnClickListener {
                binding.textEditSearchSheet.text?.clear()
                searchLogic("")

                binding.textEditSearchSheet.clearFocus()
            }

        } else {
            binding.textEditSearchSheet.visibility = View.GONE
        }

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
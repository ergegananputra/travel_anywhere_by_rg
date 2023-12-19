package com.ppb.travelanywhere.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.ppb.travelanywhere.adapter.UsersManagerAdapter
import com.ppb.travelanywhere.databinding.FragmentManagerBinding
import com.ppb.travelanywhere.services.api.FireAuth
import com.ppb.travelanywhere.services.api.FireConsole
import com.ppb.travelanywhere.services.model.User
import kotlinx.coroutines.launch

class ManagerFragment : Fragment() {

    private val binding by lazy {
        FragmentManagerBinding.inflate(layoutInflater)
    }

    private val usersManagerAdapter by lazy {
        UsersManagerAdapter(lifecycleScope)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeRefresh()
        setupRecycler()
        setupSearchBar()
        updateData()

        informationCard()

        floatingButton()
    }

    private fun informationCard() {
        binding.materialCardView5.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.constraintLayoutManager)
            if (binding.textViewManagerDescription.visibility == View.GONE) {
                binding.textViewManagerDescription.visibility = View.VISIBLE
            } else {
                binding.textViewManagerDescription.visibility = View.GONE
            }
        }
    }

    private fun floatingButton() {
        binding.floatingActionButtonDatabase.setOnClickListener {
            val action = ManagerFragmentDirections.actionManagerFragmentToInformationManagerFragment()
            findNavController().navigate(action)
        }
    }



    private fun setupSearchBar() {
        binding.textEditSearch.doAfterTextChanged {
            lifecycleScope.launch {
                val query = it.toString()
                val results = FireAuth().searchUserLocal(query)
                usersManagerAdapter.submitList(results)
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayoutManager.setOnRefreshListener {
            updateData()
            binding.swipeRefreshLayoutManager.isRefreshing = false
        }
    }

    private fun setupRecycler() {
        binding.recyclerViewManager.apply {
            adapter = usersManagerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun updateData() {
        binding.progressBarManager.visibility = View.VISIBLE
        FireConsole().usersRef.get().addOnSuccessListener {
            val usersList: MutableList<User> = it.toObjects(User::class.java)
            usersManagerAdapter.submitList(usersList)
            binding.progressBarManager.visibility = View.GONE
        }.addOnFailureListener {
            binding.progressBarManager.visibility = View.GONE
            Toast.makeText(requireContext(), "Error : ${it.message}", Toast.LENGTH_SHORT).show()
        }


    }


}
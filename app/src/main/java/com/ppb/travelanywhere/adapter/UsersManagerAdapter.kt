package com.ppb.travelanywhere.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ppb.travelanywhere.databinding.ItemUserBinding
import com.ppb.travelanywhere.services.api.FireAuth
import com.ppb.travelanywhere.services.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersManagerAdapter(
    private val scope: CoroutineScope
) : ListAdapter<User, UsersManagerAdapter.ItemUserViewHolder>(UserDiffUtil()) {
    inner class ItemUserViewHolder(
        private val binding : ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                textViewUsernameItem.text = user.username

                buttonRole.text = user.role.removePrefix("Role_").lowercase()
                buttonRole.setOnClickListener {
                    scope.launch {
                        val newRole = FireAuth().updateUserRole(user.id, user.role)
                        buttonRole.text = newRole.removePrefix("Role_").lowercase()
                    }
                }
                buttonDeleteUser.setOnClickListener {
                    scope.launch {
                        val result = FireAuth().deleteUser(user.id)
                        if (result) {
                            val position = currentList.indexOf(user)
                            val updatedList = currentList.toMutableList()
                            updatedList.removeAt(position)
                            submitList(updatedList)
                            Toast.makeText(binding.root.context, "User ${user.username} berhasil dihapus", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(binding.root.context, "User ${user.username} gagal dihapus", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

            }
        }

    }

    class UserDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemUserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ItemUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemUserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
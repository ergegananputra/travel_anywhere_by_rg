package com.ppb.travelanywhere.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ppb.travelanywhere.databinding.ItemRecyclerGlobalBinding


class GlobalTypeAdapter<T>(
    private val list: List<T>,
    var onClickItemListener: (T) -> Unit,
    private val textLabelLogic: (ItemRecyclerGlobalBinding, T) -> Unit
) : RecyclerView.Adapter<GlobalTypeAdapter<T>.GlobalTypeViewHolder>() {
    inner class GlobalTypeViewHolder(
        private val binding : ItemRecyclerGlobalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(t: T) {
            with(binding) {
                textLabelLogic(this, t)

                cardViewOptionItem.setOnClickListener {
                    onClickItemListener(t)
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalTypeViewHolder {
        val binding = ItemRecyclerGlobalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return GlobalTypeViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: GlobalTypeViewHolder, position: Int) {
        holder.bind(list[position])
    }


}
package com.roger.petadoption.ui.main.hospital

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.roger.petadoption.databinding.ItemFilterRegionBinding

class FilterRegionAdapter(val clickEvent: (FilterRegion?) -> Unit) :
    ListAdapter<FilterRegion, FilterRegionAdapter.ViewHolder>(DiffCallback) {

    var selectionType: FilterRegion? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFilterRegionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.onBind(item)
    }

    inner class ViewHolder(val binding: ItemFilterRegionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val type = currentList[bindingAdapterPosition] ?: return@setOnClickListener
                if (binding.root.isChecked) {
                    selectionType = null
                    clickEvent.invoke(null)
                } else {
                    selectionType = type
                    clickEvent.invoke(type)
                }
            }
        }

        fun onBind(value: FilterRegion) {
            binding.run {
                tvRegion.text = value.content
                root.isChecked = selectionType == value
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FilterRegion>() {
        override fun areItemsTheSame(
            oldItem: FilterRegion,
            newItem: FilterRegion,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FilterRegion,
            newItem: FilterRegion,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
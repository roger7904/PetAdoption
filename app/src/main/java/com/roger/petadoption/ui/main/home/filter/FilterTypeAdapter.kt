package com.roger.petadoption.ui.main.home.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.roger.petadoption.databinding.ItemFilterTypeBinding

class FilterTypeAdapter(val clickEvent: (FilterType?) -> Unit) :
    ListAdapter<FilterType, FilterTypeAdapter.ViewHolder>(DiffCallback) {

    var selectionType: FilterType? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFilterTypeBinding.inflate(
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

    inner class ViewHolder(val binding: ItemFilterTypeBinding) :
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

        fun onBind(value: FilterType) {
            val context = itemView.context
            binding.run {
                tvType.text = value.content
                ivType.setImageDrawable(AppCompatResources.getDrawable(context, value.iconResId))
                root.isChecked = selectionType == value
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FilterType>() {
        override fun areItemsTheSame(
            oldItem: FilterType,
            newItem: FilterType,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FilterType,
            newItem: FilterType,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
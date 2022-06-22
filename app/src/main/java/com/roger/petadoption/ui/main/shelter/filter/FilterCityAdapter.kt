package com.roger.petadoption.ui.main.shelter.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.roger.petadoption.databinding.ItemFilterCityBinding

class FilterCityAdapter(val clickEvent: (FilterCity?) -> Unit) :
    ListAdapter<FilterCity, FilterCityAdapter.ViewHolder>(DiffCallback) {

    var selectionType: FilterCity? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFilterCityBinding.inflate(
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

    inner class ViewHolder(val binding: ItemFilterCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val bodyType = currentList[bindingAdapterPosition] ?: return@setOnClickListener
                if (binding.root.isChecked) {
                    selectionType = null
                    clickEvent.invoke(null)
                } else {
                    selectionType = bodyType
                    clickEvent.invoke(bodyType)
                }
            }
        }

        fun onBind(value: FilterCity) {
            val context = itemView.context
            binding.run {
                tvCity.text = value.content
                ivCity.setImageDrawable(AppCompatResources.getDrawable(context,
                    value.iconResId))
                root.isChecked = selectionType == value
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FilterCity>() {
        override fun areItemsTheSame(
            oldItem: FilterCity,
            newItem: FilterCity,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FilterCity,
            newItem: FilterCity,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
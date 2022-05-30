package com.roger.petadoption.ui.main.home.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.roger.petadoption.databinding.ItemFilterBodyTypeBinding

class FilterBodyTypeAdapter(val clickEvent: (FilterBodyType?) -> Unit) :
    ListAdapter<FilterBodyType, FilterBodyTypeAdapter.ViewHolder>(DiffCallback) {

    var selectionType: FilterBodyType? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFilterBodyTypeBinding.inflate(
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

    inner class ViewHolder(val binding: ItemFilterBodyTypeBinding) :
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

        fun onBind(value: FilterBodyType) {
            val context = itemView.context
            binding.run {
                tvBodyType.text = value.content
                ivBodyType.setImageDrawable(AppCompatResources.getDrawable(context,
                    value.iconResId))
                root.isChecked = selectionType == value
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FilterBodyType>() {
        override fun areItemsTheSame(
            oldItem: FilterBodyType,
            newItem: FilterBodyType,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FilterBodyType,
            newItem: FilterBodyType,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
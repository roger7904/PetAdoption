package com.roger.petadoption.ui.main.home.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.roger.petadoption.databinding.ItemFilterColorBinding

class FilterColorAdapter(val clickEvent: (FilterColor?) -> Unit) :
    ListAdapter<FilterColor, FilterColorAdapter.ViewHolder>(DiffCallback) {

    var selectionType: FilterColor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFilterColorBinding.inflate(
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

    inner class ViewHolder(val binding: ItemFilterColorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val color = currentList[bindingAdapterPosition] ?: return@setOnClickListener
                if (binding.root.isChecked) {
                    selectionType = null
                    clickEvent.invoke(null)
                } else {
                    selectionType = color
                    clickEvent.invoke(color)
                }
            }
        }

        fun onBind(value: FilterColor) {
            val context = itemView.context
            binding.run {
                tvColor.text = value.content
                ivColor.setImageDrawable(AppCompatResources.getDrawable(context, value.iconResId))
                root.isChecked = selectionType == value
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FilterColor>() {
        override fun areItemsTheSame(
            oldItem: FilterColor,
            newItem: FilterColor,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FilterColor,
            newItem: FilterColor,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
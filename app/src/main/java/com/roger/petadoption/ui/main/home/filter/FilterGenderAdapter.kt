package com.roger.petadoption.ui.main.home.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.roger.petadoption.databinding.ItemFilterGenderBinding

class FilterGenderAdapter(val clickEvent: (FilterGender?) -> Unit) :
    ListAdapter<FilterGender, FilterGenderAdapter.ViewHolder>(DiffCallback) {

    var selectionType: FilterGender? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFilterGenderBinding.inflate(
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

    inner class ViewHolder(val binding: ItemFilterGenderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val gender = currentList[bindingAdapterPosition] ?: return@setOnClickListener
                if (binding.root.isChecked) {
                    selectionType = null
                    clickEvent.invoke(null)
                } else {
                    selectionType = gender
                    clickEvent.invoke(gender)
                }
            }
        }

        fun onBind(value: FilterGender) {
            val context = itemView.context
            binding.run {
                tvGender.text = value.content
                ivGender.setImageDrawable(AppCompatResources.getDrawable(context, value.iconResId))
                root.isChecked = selectionType == value
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FilterGender>() {
        override fun areItemsTheSame(
            oldItem: FilterGender,
            newItem: FilterGender,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FilterGender,
            newItem: FilterGender,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
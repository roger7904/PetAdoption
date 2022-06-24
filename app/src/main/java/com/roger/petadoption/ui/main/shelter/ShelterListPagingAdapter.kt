package com.roger.petadoption.ui.main.shelter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.roger.domain.entity.shelter.ShelterEntity
import com.roger.petadoption.databinding.ItemShelterBinding

class ShelterListPagingAdapter(
    private val clickEvent: ((shelterEntity: ShelterEntity) -> Unit),
) : PagingDataAdapter<ShelterEntity, ShelterListPagingAdapter.ViewHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemShelterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.onBind(it) }

    }

    inner class ViewHolder(val binding: ItemShelterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val pet = getItem(bindingAdapterPosition) ?: return@setOnClickListener
                clickEvent.invoke(pet)
            }
        }

        fun onBind(value: ShelterEntity) {
            binding.run {
                tvShelterName.text = value.shelterName
                if (value.phone.isNullOrEmpty()) tvShelterMobile.visibility = View.GONE
                tvShelterMobile.text = value.phone
                tvShelterLocation.text = value.address
                tvShelterCity.text = value.cityName
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ShelterEntity>() {
        override fun areItemsTheSame(
            oldItem: ShelterEntity,
            newItem: ShelterEntity,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ShelterEntity,
            newItem: ShelterEntity,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
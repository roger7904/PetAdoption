package com.roger.petadoption.ui.main.hospital

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.petadoption.databinding.ItemHospitalBinding

class HospitalListPagingAdapter(
    private val clickEvent: ((hospitalEntity: HospitalEntity) -> Unit),
) : PagingDataAdapter<HospitalEntity, HospitalListPagingAdapter.ViewHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHospitalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.onBind(it) }

    }

    inner class ViewHolder(val binding: ItemHospitalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val pet = getItem(bindingAdapterPosition) ?: return@setOnClickListener
                clickEvent.invoke(pet)
            }
        }

        fun onBind(value: HospitalEntity) {
            binding.run {
                tvHospitalName.text = value.name
                if (value.mobile.isNullOrEmpty()) tvHospitalMobile.visibility = View.GONE
                tvHospitalMobile.text = value.mobile
                tvHospitalLocation.text = value.location
                tvHospitalCity.text = value.city
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<HospitalEntity>() {
        override fun areItemsTheSame(
            oldItem: HospitalEntity,
            newItem: HospitalEntity,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: HospitalEntity,
            newItem: HospitalEntity,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
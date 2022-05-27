package com.roger.petadoption.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.roger.domain.entity.pet.PetEntity
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ItemPetInfoBinding

class PetListPagingAdapter(
    private val clickEvent: ((petEntity: PetEntity) -> Unit),
) : PagingDataAdapter<PetEntity, PetListPagingAdapter.ViewHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPetInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.onBind(it) }

    }

    inner class ViewHolder(val binding: ItemPetInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val pet = getItem(bindingAdapterPosition) ?: return@setOnClickListener
                clickEvent.invoke(pet)
            }
        }

        fun onBind(value: PetEntity) {
            binding.run {
                Glide.with(itemView)
                    .load(value.albumFile)
                    .into(ivCover)

                tvVariety.text = value.variety
                tvLocation.text = value.petPlace
                ivGender.setImageResource(if (value.sex == "M") R.drawable.ic_male else R.drawable.ic_female)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PetEntity>() {
        override fun areItemsTheSame(
            oldItem: PetEntity,
            newItem: PetEntity,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PetEntity,
            newItem: PetEntity,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
package com.roger.petadoption.ui.main.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.roger.domain.entity.pet.PetEntity
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ItemFavoritePetBinding

class FavoritePetAdapter(
    private val clickEvent: ((petEntity: PetEntity) -> Unit),
    private val removeEvent: ((petEntity: PetEntity) -> Unit),
) : ListAdapter<PetEntity, FavoritePetAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFavoritePetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.onBind(item)
    }

    inner class ViewHolder(val binding: ItemFavoritePetBinding) :
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
                    .placeholder(R.color.colorNeutral_N4_placeholder)
                    .into(ivCover)

                tvVariety.text = value.variety
                tvLocation.text = value.petPlace
                ivGender.setImageResource(if (value.sex == "M") R.drawable.ic_male else R.drawable.ic_female)

                cvRemove.setOnClickListener {
                    val pet = getItem(bindingAdapterPosition) ?: return@setOnClickListener
                    removeEvent.invoke(pet)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PetEntity>() {
        override fun areItemsTheSame(oldItem: PetEntity, newItem: PetEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PetEntity, newItem: PetEntity): Boolean {
            return oldItem == newItem
        }
    }
}
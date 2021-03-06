package com.roger.petadoption.ui.main.home

import android.os.SystemClock
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
    private val favoriteEvent: ((petEntity: PetEntity) -> Unit),
    private val closeEvent: ((position: Int) -> Unit),
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
            var mLastClickTime: Long = 0
            binding.run {
                Glide.with(itemView)
                    .load(value.albumFile)
                    .placeholder(R.color.colorNeutral_N4_placeholder)
                    .into(ivCover)

                tvVariety.text = value.variety
                tvLocation.text = value.petPlace
                ivGender.setImageResource(if (value.sex == "M") R.drawable.ic_male else R.drawable.ic_female)

                cvFavorite.setOnClickListener {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return@setOnClickListener
                    }
                    mLastClickTime = SystemClock.elapsedRealtime()

                    val pet = getItem(bindingAdapterPosition) ?: return@setOnClickListener
                    favoriteEvent.invoke(pet)
                }

                cvClose.setOnClickListener {
                    closeEvent.invoke(bindingAdapterPosition)
                }
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
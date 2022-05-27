package com.roger.petadoption.ui.main.home

import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ActivityPetDetailBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PetDetailActivity : BaseActivity<ActivityPetDetailBinding>() {

    private val viewModel: PetDetailViewModel by viewModels()

    override fun initParam(data: Bundle) {
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initViewBinding(): ActivityPetDetailBinding =
        ActivityPetDetailBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            cvClose.setOnClickListener {
                onBackPressed()
            }

            viewModel.petInfo.observe(this@PetDetailActivity) {
                Glide.with(this@PetDetailActivity)
                    .load(it.albumFile)
                    .into(ivCover)

                tvVariety.text = it.variety
                tvLocation.text = it.petPlace
                ivGender.setImageResource(if (it.sex == ANIMAL_GENDER_MALE) R.drawable.ic_male else R.drawable.ic_female)
                ivGender2.setImageResource(if (it.sex == ANIMAL_GENDER_MALE) R.drawable.ic_male else R.drawable.ic_female)
                tvGender.text =
                    if (it.sex == ANIMAL_GENDER_MALE) getString(R.string.male) else getString(
                        R.string.female)
                tvState.text =
                    if (it.status == ANIMAL_STATE_OPEN) getString(R.string.home_pet_state_pos) else getString(
                        R.string.home_pet_state_neg)
                tvColor.text = it.colour
                tvShelterContent.text = it.shelterName
                tvShelterAddressContent.text = it.shelterAddress
                tvShelterMobileContent.text = it.shelterTel
                tvUpdateTimeContent.text = it.infoUpdateTime
                tvRemarkContent.text =
                    if (it.remark.isNullOrEmpty()) getString(R.string.home_pet_remark_title) else it.remark
            }
        }
    }

    companion object {
        const val ARG_PET_ID = "PET_ID"
        const val ANIMAL_STATE_OPEN = "OPEN"
        const val ANIMAL_GENDER_MALE = "M"
    }
}
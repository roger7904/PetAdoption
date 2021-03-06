package com.roger.petadoption.ui.main.home.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ActivityPetDetailBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.SimpleDialogFragment
import com.roger.petadoption.ui.base.ViewEvent
import com.roger.petadoption.ui.main.home.HomeFragment
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
                    .placeholder(R.color.colorNeutral_N4_placeholder)
                    .into(ivCover)

                tvVariety.text = it.variety
                tvLocation.text = it.petPlace
                ivGender.setImageResource(if (it.sex == ANIMAL_GENDER_MALE) R.drawable.ic_male else R.drawable.ic_female)
                ivGender2.setImageResource(if (it.sex == ANIMAL_GENDER_MALE) R.drawable.ic_male else R.drawable.ic_female)
                tvGender.text =
                    if (it.sex == ANIMAL_GENDER_MALE) getString(R.string.male) else getString(
                        R.string.female
                    )
                tvState.text =
                    if (it.status == ANIMAL_STATE_OPEN) getString(R.string.home_pet_state_pos) else getString(
                        R.string.home_pet_state_neg
                    )
                tvColor.text = it.colour
                tvShelterContent.text = it.shelterName
                tvShelterAddressContent.text = it.shelterAddress
                tvShelterMobileContent.text = it.shelterTel
                tvUpdateTimeContent.text = it.infoUpdateTime
                tvRemarkContent.text =
                    if (it.remark.isNullOrEmpty()) getString(R.string.home_pet_remark_title) else it.remark

                llShelter.setOnClickListener { _ ->
                    val intent =
                        Intent(this@PetDetailActivity, ShelterMapActivity::class.java).apply {
                            putExtra(ShelterMapActivity.ARG_PET_MAP_ID, it.id)
                        }
                    startActivity(intent)
                }
            }

            cvFavorite.setOnClickListener {
                viewModel.setFavorite()
            }

            btnIntentToWeb.setOnClickListener {
                showDialog(
                    SimpleDialogFragment.newInstance(
                        getString(R.string.home_intent_to_web_dialog_title),
                        getString(
                            R.string.home_intent_to_web_dialog_content,
                            viewModel.petInfo.value?.subId
                        ),
                        btnConfirm = getString(R.string.confirm),
                        btnCancel = getString(R.string.cancel),
                        clickEvent = object : SimpleDialogFragment.ClickEvent {
                            override fun onConfirmClick() {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse(
                                        getString(
                                            R.string.home_intent_to_web_url,
                                            viewModel.petInfo.value?.id,
                                            viewModel.petInfo.value?.subId,
                                        )
                                    )
                                }
                                startActivity(intent)
                            }

                            override fun onCancelClick() {
                            }
                        }
                    )
                )
            }

            viewModel.isFavorite.observe(this@PetDetailActivity) {
                ivFavorite.setImageResource(if (it == true) R.drawable.ic_favorite else R.drawable.ic_favorite_inactive)
            }

            viewModel.isFromFavorite.observe(this@PetDetailActivity) {
                if (it) {
                    cvFavorite.visibility = View.GONE
                }
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent().apply {
            putExtra(HomeFragment.ARG_IS_SET_FAVORITE, viewModel.isFavorite.value)
        }
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }

    override fun handleViewEvent(event: ViewEvent) {
        super.handleViewEvent(event)
        when (event) {
            is PetDetailViewEvent.SetFavoriteSuccess -> {
                Toast.makeText(
                    this, getString(R.string.home_set_favorite_success), Toast.LENGTH_SHORT
                ).show()
            }
            is PetDetailViewEvent.SetNotFavoriteSuccess -> {
                Toast.makeText(
                    this, getString(R.string.home_set_not_favorite_success), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val ARG_PET_ID = "PET_ID"
        const val ARG_IS_FROM_FAVORITE = "IS_FROM_FAVORITE"
        const val ANIMAL_STATE_OPEN = "OPEN"
        const val ANIMAL_GENDER_MALE = "M"
    }
}
package com.roger.petadoption.ui.main.hospital.detail

import android.os.Bundle
import androidx.activity.viewModels
import com.roger.petadoption.databinding.ActivityHospitalDetailBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.main.home.detail.PetDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HospitalDetailActivity : BaseActivity<ActivityHospitalDetailBinding>() {

    private val viewModel: PetDetailViewModel by viewModels()

    override fun initParam(data: Bundle) {
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initViewBinding(): ActivityHospitalDetailBinding =
        ActivityHospitalDetailBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {

    }
}
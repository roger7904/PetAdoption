package com.roger.petadoption.ui.auth

import android.os.Bundle
import androidx.activity.viewModels
import com.roger.petadoption.databinding.ActivitySignUpBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {

    private val viewModel: SignUpViewModel by viewModels()

    override fun initParam(data: Bundle) {
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initViewBinding(): ActivitySignUpBinding =
        ActivitySignUpBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {

    }
}
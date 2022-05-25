package com.roger.petadoption.ui.launch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.roger.petadoption.databinding.ActivityLaunchBinding
import com.roger.petadoption.ui.auth.SignInActivity
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.ViewEvent
import com.roger.petadoption.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchActivity : BaseActivity<ActivityLaunchBinding>() {

    private val viewModel: LaunchViewModel by viewModels()

    override fun initParam(data: Bundle) {
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initViewBinding(): ActivityLaunchBinding =
        ActivityLaunchBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.checkLaunchMode()
        }, 1000)
    }

    override fun handleViewEvent(event: ViewEvent) {
        super.handleViewEvent(event)
        when (event) {
            is LaunchViewEvent.LoginRequired -> {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }

            is LaunchViewEvent.AuthPassed -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }
}
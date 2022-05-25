package com.roger.petadoption.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.jakewharton.rxbinding4.widget.textChanges
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ActivityEditProfileBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.ViewEvent
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.kotlin.addTo

@AndroidEntryPoint
class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>() {

    private val viewModel: EditProfileViewModel by viewModels()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initViewBinding(): ActivityEditProfileBinding =
        ActivityEditProfileBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            ivBack.setOnClickListener {
                finish()
            }

            with(tilNickname) {
                getEditText()?.textChanges()?.skipInitialValue()?.subscribe {
                    viewModel.setUserName(it.toString())
                }?.addTo(compositeDisposable)
            }

            viewModel.userEmail.observe(this@EditProfileActivity) {
                tilEmail.text = it
            }

            viewModel.userName.observe(this@EditProfileActivity) {
                tilNickname.text = it
            }

            tvSave.run {
                setOnClickListener {
                    viewModel.updateUser()
                }
            }
        }
    }

    override fun handleViewEvent(event: ViewEvent) {
        super.handleViewEvent(event)
        when (event) {
            is EditProfileViewEvent.UpdateUserSuccess -> {
                Toast.makeText(
                    this, getString(R.string.editProfile_success), Toast.LENGTH_SHORT
                ).show()
                val intent = Intent().apply {
                    putExtra(BUNDLE_USER_NAME, event.useName)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    companion object {
        private const val BUNDLE_USER_NAME = "name"
    }
}
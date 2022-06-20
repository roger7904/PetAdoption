package com.roger.petadoption.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding4.view.clicks
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ActivitySignUpBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.SimpleDialogFragment
import com.roger.petadoption.ui.base.ViewEvent
import com.roger.petadoption.ui.main.MainActivity
import com.roger.petadoption.utils.isPasswordValid
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {

    private val viewModel: SignUpViewModel by viewModels()

    override fun initParam(data: Bundle) {
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initViewBinding(): ActivitySignUpBinding =
        ActivitySignUpBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            btnRegister.clicks()
                .subscribeBy {
                    if (tilAccount.getEditText()?.text?.toString().isNullOrEmpty() ||
                        tilPassword.getEditText()?.text?.toString().isNullOrEmpty()
                    ) {
                        val dialog = SimpleDialogFragment.newInstance(
                            title = getString(R.string.sign_in_empty_title),
                            content = getString(R.string.sign_in_empty_content),
                            btnConfirm = getString(R.string.confirm),
                            btnCancel = getString(R.string.cancel)
                        )
                        showDialog(dialog)
                        return@subscribeBy
                    }
                    if (!tilPassword.getEditText()?.text?.toString().isPasswordValid()) {
                        val dialog = SimpleDialogFragment.newInstance(
                            title = getString(R.string.sign_in_password_invalid_title),
                            content = getString(R.string.sign_in_password_invalid_content),
                            btnConfirm = getString(R.string.confirm),
                            btnCancel = getString(R.string.cancel)
                        )
                        showDialog(dialog)
                        return@subscribeBy
                    }
                    viewModel.passwordSignUp(
                        tilAccount.getEditText()?.text?.toString(),
                        tilPassword.getEditText()?.text?.toString()
                    )
                }
                .addTo(compositeDisposable)

            with(tvGoSignIn) {
                val ssb =
                    SpannableStringBuilder(getString(R.string.sign_in_go_sign_in)).apply {
                        setSpan(
                            ForegroundColorSpan(
                                ContextCompat.getColor(
                                    context,
                                    R.color.colorBlue_BL3_action
                                )
                            ),
                            length - 4,
                            length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                text = ssb
                setOnClickListener {
                    val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun handleViewEvent(event: ViewEvent) {
        super.handleViewEvent(event)
        when (event) {
            is SignUpViewEvent.SignUpSuccess -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            is SignUpViewEvent.SignUpFail -> {
                val dialog = SimpleDialogFragment.newInstance(
                    title = getString(R.string.sign_up_fail_title),
                    content = getString(R.string.sign_up_fail_content),
                    btnConfirm = getString(R.string.confirm),
                    btnCancel = getString(R.string.cancel)
                )
                showDialog(dialog)
            }
        }
    }
}
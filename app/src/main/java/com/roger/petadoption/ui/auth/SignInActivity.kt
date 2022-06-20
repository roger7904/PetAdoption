package com.roger.petadoption.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.jakewharton.rxbinding4.view.clicks
import com.roger.petadoption.BuildConfig
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ActivitySignInBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.SimpleDialogFragment
import com.roger.petadoption.ui.base.ViewEvent
import com.roger.petadoption.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

@AndroidEntryPoint
class SignInActivity : BaseActivity<ActivitySignInBinding>() {

    private val viewModel: SignInViewModel by viewModels()
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == GOOGLE_SIGN_IN_RESULT_SUCCESS) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleGoogleSignInResult(task)
            }
        }

    override fun initParam(data: Bundle) {
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initViewBinding(): ActivitySignInBinding =
        ActivitySignInBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        initFacebookCallbackManager()
        initGoogleSignInClient()

        binding?.run {
            clFacebook.clicks()
                .subscribeBy {
                    LoginManager.getInstance().logInWithReadPermissions(
                        this@SignInActivity,
                        callbackManager,
                        listOf("public_profile", "email")
                    )
                }
                .addTo(compositeDisposable)

            clGoogle.clicks()
                .subscribeBy {
                    mGoogleSignInClient?.revokeAccess()?.addOnCompleteListener {
                        googleSignInLauncher.launch(mGoogleSignInClient?.signInIntent)
                    }
                }
                .addTo(compositeDisposable)

            btnSignIn.clicks()
                .subscribeBy {
                    viewModel.passwordSignIn(
                        tilAccount.getEditText()?.text?.toString(),
                        tilPassword.getEditText()?.text?.toString()
                    )
                }
                .addTo(compositeDisposable)

            with(tvRegister) {
                val ssb =
                    SpannableStringBuilder(getString(R.string.sign_in_register)).apply {
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
                    val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun handleViewEvent(event: ViewEvent) {
        super.handleViewEvent(event)
        when (event) {
            is SignInViewEvent.LoginSuccess -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            is SignInViewEvent.LoginFail -> {
                val dialog = SimpleDialogFragment.newInstance(
                    title = getString(R.string.sign_in_authentication_failed),
                    content = getString(R.string.sign_in_account_already_exists),
                    btnConfirm = getString(R.string.confirm),
                    btnCancel = getString(R.string.cancel)
                )
                showDialog(dialog)
            }

            is SignInViewEvent.PasswordLoginFail -> {
                val dialog = SimpleDialogFragment.newInstance(
                    title = getString(R.string.sign_in_password_login_failed_title),
                    content = getString(R.string.sign_in_password_login_failed_content),
                    btnConfirm = getString(R.string.confirm),
                    btnCancel = getString(R.string.cancel)
                )
                showDialog(dialog)
            }
        }
    }

    private fun initFacebookCallbackManager() {
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    if (result?.accessToken != null) {
                        handleFacebookSignInResult(result.accessToken)
                    }
                }

                override fun onCancel() {}
                override fun onError(error: FacebookException) {}
            })
    }

    private fun initGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val googleIdToken = task.result.idToken
            val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
            viewModel.firebaseLogin(credential)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleFacebookSignInResult(token: AccessToken) {
        try {
            val credential = FacebookAuthProvider.getCredential(token.token)
            viewModel.firebaseLogin(credential)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val GOOGLE_SIGN_IN_RESULT_SUCCESS = -1
    }
}
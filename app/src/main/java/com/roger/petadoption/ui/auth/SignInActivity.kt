package com.roger.petadoption.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding4.view.clicks
import com.roger.petadoption.BuildConfig
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ActivitySignInBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.SimpleDialogFragment
import com.roger.petadoption.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

@AndroidEntryPoint
class SignInActivity : BaseActivity<ActivitySignInBinding>() {

    private val viewModel: SignInViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
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
        auth = Firebase.auth
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
            // switch to viewModel
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this) { taskFirebase ->
                    if (taskFirebase.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success")
                        val user = auth.currentUser
                        //updateUI(user)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val dialog = SimpleDialogFragment.newInstance(
                            title = getString(R.string.sign_in_authentication_failed),
                            content = null,
                            btnConfirm = getString(R.string.confirm),
                            btnCancel = getString(R.string.cancel)
                        )
                        showDialog(dialog)
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleFacebookSignInResult(token: AccessToken) {
        try {
            val credential = FacebookAuthProvider.getCredential(token.token)
            // switch to viewModel
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success")
                        val user = auth.currentUser
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val dialog = SimpleDialogFragment.newInstance(
                            title = getString(R.string.sign_in_authentication_failed),
                            content = getString(R.string.sign_in_account_already_exists),
                            btnConfirm = getString(R.string.confirm),
                            btnCancel = getString(R.string.cancel)
                        )
                        showDialog(dialog)
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val GOOGLE_SIGN_IN_RESULT_SUCCESS = -1
    }
}
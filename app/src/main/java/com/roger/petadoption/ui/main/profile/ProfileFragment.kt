package com.roger.petadoption.ui.main.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.roger.petadoption.BuildConfig
import com.roger.petadoption.R
import com.roger.petadoption.databinding.FragmentProfileBinding
import com.roger.petadoption.ui.auth.SignInActivity
import com.roger.petadoption.ui.base.BaseFragment
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.SimpleDialogFragment
import com.roger.petadoption.ui.base.ViewEvent
import com.roger.petadoption.ui.main.profile.edit_profile.EditProfileActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()
    private var auth: FirebaseAuth = Firebase.auth

    private val updateUserLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK)
                return@registerForActivityResult
            val intent = result.data
            intent?.getStringExtra(BUNDLE_USER_NAME)?.let { viewModel.setUserName(it) }
        }

    override fun initParam(data: Bundle) {
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun bindFragmentListener(context: Context) {
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            Glide.with(this@ProfileFragment)
                .load(auth.currentUser?.photoUrl)
                .centerCrop()
                .into(ivAvatar)

            viewModel.userName.observe(viewLifecycleOwner) {
                tvUserName.text = it
            }

            clProfile.setOnClickListener {
                val intent = Intent(activity, EditProfileActivity::class.java)
                updateUserLauncher.launch(intent)
            }

            tvVersionName.text =
                getString(R.string.profile_version_number, BuildConfig.VERSION_NAME)

            clSignOut.setOnClickListener {
                showDialog(
                    SimpleDialogFragment.newInstance(
                        getString(R.string.profile_sign_out_dialog_title),
                        getString(R.string.profile_sign_out_dialog_content),
                        btnConfirm = getString(R.string.confirm),
                        btnCancel = getString(R.string.cancel),
                        clickEvent = object : SimpleDialogFragment.ClickEvent {
                            override fun onConfirmClick() {
                                viewModel.signOut()
                            }

                            override fun onCancelClick() {
                            }
                        }
                    )
                )
            }
        }
    }

    override fun handleViewEvent(event: ViewEvent) {
        super.handleViewEvent(event)
        when (event) {
            ProfileViewEvent.SignOut -> {
                val intent = Intent(activity, SignInActivity::class.java)
                startActivity(intent)
                requireActivity().finishAffinity()
            }
        }
    }

    companion object {

        private const val BUNDLE_USER_NAME = "name"

        @JvmStatic
        fun newInstance() = ProfileFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}
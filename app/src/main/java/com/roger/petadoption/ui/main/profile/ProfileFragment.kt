package com.roger.petadoption.ui.main.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.roger.petadoption.R
import com.roger.petadoption.databinding.FragmentProfileBinding
import com.roger.petadoption.ui.auth.SignInActivity
import com.roger.petadoption.ui.base.BaseFragment
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.SimpleDialogFragment
import com.roger.petadoption.ui.base.ViewEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()
    private var auth: FirebaseAuth = Firebase.auth

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
        @JvmStatic
        fun newInstance() = ProfileFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}
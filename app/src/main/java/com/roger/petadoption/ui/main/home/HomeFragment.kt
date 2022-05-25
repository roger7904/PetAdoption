package com.roger.petadoption.ui.main.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.roger.petadoption.databinding.FragmentHomeBinding
import com.roger.petadoption.ui.base.BaseFragment
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    override fun initParam(data: Bundle) {
    }

    override fun getViewModel(): BaseViewModel? = viewModel

    override fun bindFragmentListener(context: Context) {
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}
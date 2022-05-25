package com.roger.petadoption.ui.main.map

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.roger.petadoption.databinding.FragmentMapBinding
import com.roger.petadoption.ui.base.BaseFragment
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>() {

    private val viewModel: MapViewModel by viewModels()

    override fun initParam(data: Bundle) {
    }

    override fun getViewModel(): BaseViewModel? = viewModel

    override fun bindFragmentListener(context: Context) {
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMapBinding = FragmentMapBinding.inflate(inflater, container, false)

    override fun initView(savedInstanceState: Bundle?) {
    }

    companion object {
        @JvmStatic
        fun newInstance() = MapFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}